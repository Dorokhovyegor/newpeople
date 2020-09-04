package com.nullit.newpeople.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.nullit.newpeople.R
import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.broadcastreceiver.QueueBroadcastReceiver.Companion.BROADCAST_VIDEO_ACTION
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.VideoDao
import com.nullit.newpeople.room.entity.VideoProperty
import com.nullit.newpeople.util.getFlagFromBroadcast
import com.nullit.newpeople.util.getVideoPath
import com.nullit.newpeople.util.getViolationId
import com.nullit.newpeople.util.putHasVideo
import dagger.android.DaggerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
        // переменные
        var violationId: Int
        var videoPath: String
        // заходим проверяем команды
        intent?.let {
            serviceScope.launch {
                // если сообщение пришло из бродкаста, значит должны заглянуть в базу
                if (it.getFlagFromBroadcast()) {
                    val videoInfo = videoDao.getDoesNotUploadedVideo()
                    if (videoInfo.isNotEmpty()) {
                        violationId = videoInfo[0].violationId
                        videoPath = videoInfo[0].videoPath
                    } else {
                        return@launch
                    }
                } else {
                    violationId = intent.getViolationId()
                    videoPath = intent.getVideoPath()
                    videoDao.insertVideo(VideoProperty(violationId, videoPath, false))
                }
                showNotification(violationId)
                val token = prepareToken()
                val vFile = prepareVideoFile(videoPath)
                val requestResult = mainApiService.addVideo(token = token, id = violationId, video = vFile)
                if (requestResult.code() == 200) {
                    videoDao.updateVideo(true, violationId)
                }
                val queue = videoDao.getDoesNotUploadedVideo()
                if (queue.isEmpty()) {
                    // если нет, то останавливаем
                    stopSelf()
                    stopForeground(true)
                } else {
                    // если есть, то посылаем бродкаст
                    sendBroadcast(Intent(BROADCAST_VIDEO_ACTION).apply {
                        putHasVideo(true)
                    })
                    stopForeground(true)
                }
            }
        }
        return Service.START_STICKY
    }

    private fun prepareVideoFile(videoPath: String): MultipartBody.Part {
        val videoFile = File(videoPath)
        val videoBody: RequestBody = videoFile.asRequestBody("video/*".toMediaTypeOrNull())
        return createFormData("video", videoFile.name, videoBody)
    }

    private suspend fun prepareToken(): String {
        val info = userDao.requestUserInfo()
        return "${info?.token_type} ${info?.token}"
    }

    private fun showNotification(violationId: Int) {
        val builder = NotificationCompat.Builder(this, "videoUploadChannel")
        builder.setSmallIcon(R.mipmap.image)
            .setContentText("Загрузка видео...")
            .setProgress(0, 100, true)
            .setContentTitle("Загрузка")
        val notification = builder.build()
        startForeground(violationId, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "Сервис уничтожен", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }
}