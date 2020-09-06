package com.nullit.newpeople.di.modules.main

import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.di.scopes.MainScope
import com.nullit.newpeople.repo.main.PhotoRepo
import com.nullit.newpeople.repo.main.PhotoRepoImpl
import com.nullit.newpeople.repo.main.VideoRepo
import com.nullit.newpeople.repo.main.VideoRepoImpl
import com.nullit.newpeople.room.dao.PhotoDao
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.VideoDao
import com.nullit.newpeople.room.dao.ViolationDao
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideVideoRepo(
        apiService: MainApiService,
        userDao: UserDao,
        videoDao: VideoDao,
        violationDao: ViolationDao
    ): VideoRepo {
        return VideoRepoImpl(apiService, userDao, videoDao, violationDao)
    }

    @MainScope
    @Provides
    fun providePhotoRepo(
        apiService: MainApiService,
        userDao: UserDao,
        violationDao: ViolationDao
    ): PhotoRepo {
        return PhotoRepoImpl(apiService, userDao, violationDao)
    }

}