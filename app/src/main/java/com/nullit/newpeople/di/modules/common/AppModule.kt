package com.nullit.newpeople.di.modules.common

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.nullit.newpeople.R
import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.di.scopes.AuthScope
import com.nullit.newpeople.di.scopes.MainScope
import com.nullit.newpeople.repo.auth.AuthRepository
import com.nullit.newpeople.repo.auth.AuthRepositoryImpl
import com.nullit.newpeople.room.dao.*
import com.nullit.newpeople.room.db.MainDataBase
import com.nullit.newpeople.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()


        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideMainApiService(retrofitBuilder: Retrofit.Builder): MainApiService {
        return retrofitBuilder.build().create(MainApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        application: Application,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.mipmap.image)
            .error(R.mipmap.image)

    }

    @Singleton
    @Provides
    fun providerRoomDataBase(application: Application): MainDataBase {
        return Room.databaseBuilder(application, MainDataBase::class.java, MainDataBase.DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun providerUserDao(mainDataBase: MainDataBase): UserDao {
        return mainDataBase.getUserDao()
    }

    @Singleton
    @Provides
    fun providerVideoDao(mainDataBase: MainDataBase): VideoDao {
        return mainDataBase.getVideoDao()
    }

    @Singleton
    @Provides
    fun providerPhotoDao(mainDataBase: MainDataBase): PhotoDao {
        return mainDataBase.getPhotoDao()
    }

    @Singleton
    @Provides
    fun providerProcessDao(mainDataBase: MainDataBase): ProcessDao {
        return mainDataBase.getProccessDao()
    }

    @Singleton
    @Provides
    fun providerViolationDao(mainDataBase: MainDataBase): ViolationDao {
        return mainDataBase.getViolationDao()
    }

    @Singleton
    @Provides
    fun provideAuthRepo(apiService: MainApiService, userDao: UserDao, violationDao: ViolationDao): AuthRepository {
        return AuthRepositoryImpl(apiService, userDao, violationDao)
    }
}