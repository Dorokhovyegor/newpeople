package com.nullit.newpeople.di.modules.main

import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.di.scopes.MainScope
import com.nullit.newpeople.repo.main.VideoRepo
import com.nullit.newpeople.repo.main.VideoRepoImpl
import com.nullit.newpeople.room.dao.ProcessDao
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.VideoDao
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideAuthRepo(
        apiService: MainApiService,
        userDao: UserDao,
        videoProcessDao: ProcessDao,
        videoDao: VideoDao
    ): VideoRepo {
        return VideoRepoImpl(apiService, userDao, videoProcessDao, videoDao)
    }

}