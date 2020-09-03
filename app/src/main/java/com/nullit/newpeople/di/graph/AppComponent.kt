package com.nullit.newpeople.di.graph

import android.app.Application
import com.nullit.newpeople.NewPeopleApplication
import com.nullit.newpeople.di.modules.common.ActivityBuildersModule
import com.nullit.newpeople.di.modules.common.AppModule
import com.nullit.newpeople.di.modules.common.ViewModelFactoryModule
import com.nullit.newpeople.di.modules.main.MainServiceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        MainServiceModule::class,
        ViewModelFactoryModule::class,
        AndroidInjectionModule::class,
        ActivityBuildersModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<NewPeopleApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}