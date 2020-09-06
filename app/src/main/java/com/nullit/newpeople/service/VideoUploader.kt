package com.nullit.newpeople.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.nullit.newpeople.R
import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.room.dao.ProcessDao
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.VideoDao
import dagger.android.DaggerService
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


class VideoUploader : DaggerService() {

    @Inject
    lateinit var mainApiService: MainApiService
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var videoDao: VideoDao

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            val tokenResult = async {
                prepareToken()
            }
            val token = tokenResult.await()
            val videosResult = async {
                videoDao.getDoesNotUploadedVideo()
            }
            val videos = videosResult.await()
            videos.forEach {
                val violationId = it.violationId

                val videoPath = it.videoPath
                val vFile = prepareVideoFile(videoPath)
                val apiResult = async {
                    mainApiService.addVideo(token = token, id = violationId, video = vFile)
                }
                val requestResult = apiResult.await()
                if (requestResult.code() == 200) {
                    val updateVideoResult = async {
                        videoDao.updateVideo(true, violationId)
                    }
                    updateVideoResult.await()
                }
                val queueAsynk = async {
                    videoDao.getDoesNotUploadedVideo()
                }
                val queue = queueAsynk.await()
                withContext(Dispatchers.Main) {
                    if (queue.isNotEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Осталось в очереди: ${queue.size}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            stopForeground(true)
            stopSelf()
        }
        return Service.START_NOT_STICKY
    }

    private suspend fun prepareVideoFile(videoPath: String): MultipartBody.Part {
        showNotification()
        val videoFile = File(videoPath)
        val videoBody: RequestBody = videoFile.asRequestBody("video/*".toMediaTypeOrNull())
        return createFormData("videos[0]", videoFile.name, videoBody)
    }

    private suspend fun prepareToken(): String {
        val info = userDao.requestUserInfo()
        return "${info?.token_type} ${info?.token}"
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, "videoUploadChannel")
        builder.setSmallIcon(R.mipmap.image)
            .setContentText("Загрузка видео. Не отключайте телефон")
            .setProgress(0, 100, true)
            .setContentTitle("Загрузка")
        val notification = builder.build()
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}