package com.nullit.newpeople.di.modules.main

import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.di.scopes.AuthScope
import com.nullit.newpeople.di.scopes.MainScope
import com.nullit.newpeople.repo.auth.AuthRepository
import com.nullit.newpeople.repo.auth.AuthRepositoryImpl
import com.nullit.newpeople.repo.main.VideoRepo
import com.nullit.newpeople.repo.main.VideoRepoImpl
import com.nullit.newpeople.room.dao.UserDao
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideAuthRepo(apiService: MainApiService, userDao: UserDao): VideoRepo {
        return VideoRepoImpl(apiService, userDao)
    }

}