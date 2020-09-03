package com.nullit.newpeople.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.nullit.newpeople.R
import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.util.getVideoPath
import com.nullit.newpeople.util.getViolationId
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

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val violationId = intent?.getViolationId()
        //showNotification(violationId!!)
        serviceScope.launch {
            val videoPath = intent?.getVideoPath()
            val info = userDao.requestUserInfo()
            val token = "${info?.token_type} ${info?.token}"
            if (videoPath != null && videoPath != null && token != null) {
                val videoFile = File(videoPath)
                val videoBody: RequestBody = videoFile.asRequestBody("video/*".toMediaTypeOrNull())
                val vFile: MultipartBody.Part =
                    createFormData("video", videoFile.getName(), videoBody)
                val result = mainApiService.addVideo(token = token, id = violationId!!, video = vFile)
             //   stopForeground(true)

            }
        }
        return Service.START_STICKY
    }

    private fun showNotification(violationId: Int) {
        val builder = NotificationCompat.Builder(this, "videoUploading")
        builder.setSmallIcon(R.mipmap.image)
            .setContentText("Загрузка видео...")
            .setContentTitle("Загрузка")
        val notification = builder.build()
        startForeground(violationId, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    suspend fun uploadVideo(videoPath: String) {
        //apiService.createViolation()
    }

    override fun onDestroy() {
        Toast.makeText(this, "Сервис уничтожен", Toast.LENGTH_LONG).show()
        super.onDestroy()
        Log.e("VideoUploader", "onDestroy")
    }
}