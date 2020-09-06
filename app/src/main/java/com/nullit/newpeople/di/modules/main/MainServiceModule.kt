package com.nullit.newpeople.di.modules.main

import com.nullit.newpeople.service.PhotoUploader
import com.nullit.newpeople.service.VideoUploader
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainServiceModule {

    @ContributesAndroidInjector
    abstract fun bindVideoUploaderService(): VideoUploader

    @ContributesAndroidInjector
    abstract fun bindPhotoUploaderService(): PhotoUploader
}