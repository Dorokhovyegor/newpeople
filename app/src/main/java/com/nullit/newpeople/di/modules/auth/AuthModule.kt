package com.nullit.newpeople.di.modules.auth

import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.di.scopes.AuthScope
import com.nullit.newpeople.repo.auth.AuthRepository
import com.nullit.newpeople.repo.auth.AuthRepositoryImpl
import com.nullit.newpeople.room.dao.UserDao
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule {

    @AuthScope
    @Provides
    fun provideAuthRepo(apiService: MainApiService, userDao: UserDao): AuthRepository {
        return AuthRepositoryImpl(apiService, userDao)
    }
}