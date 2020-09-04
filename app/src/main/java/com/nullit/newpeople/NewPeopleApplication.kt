package com.nullit.newpeople

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import com.nullit.newpeople.di.graph.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasServiceInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject


class NewPeopleApplication : DaggerApplication(), HasServiceInjector {

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun serviceInjector(): AndroidInjector<Service> {
        return dispatchingServiceInjector
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        createVideoNotificationChannel()
    }

    private fun createVideoNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "videoChannel"
            val descriptionText = "Description text"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("videoUploadChannel", name, importance)
            channel.description = descriptionText
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}