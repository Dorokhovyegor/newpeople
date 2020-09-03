package com.nullit.newpeople.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.room.dao.UserDao
import dagger.android.DaggerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoUploader : DaggerService() {

    @Inject
    lateinit var mainApiService: MainApiService

    @Inject
    lateinit var userDao: UserDao

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        serviceScope.launch {
            val info = userDao.requestUserInfo()
            val token = "${info?.token_type} ${info?.token}"
           // mainApiService.addVideo()
        }
        return Service.START_STICKY
    }

    private fun showNotification() {

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    suspend fun uploadVideo(videoPath: String) {
        //apiService.createViolation()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("VideoUploader", "onDestroy")
    }
}