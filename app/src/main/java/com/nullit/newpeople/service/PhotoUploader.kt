package com.nullit.newpeople.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.nullit.newpeople.R
import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.util.getPhotos
import com.nullit.newpeople.util.getViolationId
import dagger.android.DaggerService
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class PhotoUploader : DaggerService() {

    @Inject
    lateinit var mainApiService: MainApiService

    @Inject
    lateinit var userDao: UserDao
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        serviceScope.launch {
            val tokenResult = async {
                prepareToken()
            }
            val token = tokenResult.await()
            val photos = intent?.getPhotos()
            val vio = intent?.getViolationId()!!
            val photosBody = ArrayList<MultipartBody.Part>()
            photos?.withIndex()?.forEach {
                photosBody.add(preparePhotoMultipart(it.value.path, it.index))
            }
            val requestResult = async {
                mainApiService.addPhotos(token = token, id = vio, photos = photosBody)
            }
            val apiResult = requestResult.await()
            if (apiResult.code() == 200) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Файлы загружены", Toast.LENGTH_SHORT).show()
                }
            }
            stopForeground(true)
            stopSelf()
        }
        return Service.START_STICKY
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, "photoUploaderChannel")
        builder.setSmallIcon(R.mipmap.image)
            .setContentText("Загрузка изображений, не отключайте телефон")
            .setProgress(0, 100, true)
            .setContentTitle("Загрузка")
        val notification = builder.build()
        startForeground(2, notification)
    }

    private fun preparePhotoMultipart(photoPath: String, index: Int): MultipartBody.Part {
        val photoFile = File(photoPath)
        val photoBody: RequestBody = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("photos[$index]", photoFile.name, photoBody)
    }

    private suspend fun prepareToken(): String {
        val info = userDao.requestUserInfo()
        return "${info?.token_type} ${info?.token}"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}