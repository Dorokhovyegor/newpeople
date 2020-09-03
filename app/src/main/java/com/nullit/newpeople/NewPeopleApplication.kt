package com.nullit.newpeople

import android.app.Service
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

}