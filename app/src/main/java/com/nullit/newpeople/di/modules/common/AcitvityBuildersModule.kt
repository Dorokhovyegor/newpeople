package com.nullit.newpeople.di.modules.common

import com.nullit.newpeople.di.modules.auth.AuthModule
import com.nullit.newpeople.di.modules.auth.AuthViewModelModule
import com.nullit.newpeople.di.modules.main.MainFragmentBuildersModule
import com.nullit.newpeople.di.modules.main.MainModule
import com.nullit.newpeople.di.modules.main.MainServiceModule
import com.nullit.newpeople.di.modules.main.MainViewModelModule
import com.nullit.newpeople.di.scopes.AuthScope
import com.nullit.newpeople.di.scopes.MainScope
import com.nullit.newpeople.ui.auth.AuthActivity
import com.nullit.newpeople.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthModule::class, AuthViewModelModule::class, ViewModelFactoryModule::class
        ]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            ViewModelFactoryModule::class,
            MainFragmentBuildersModule::class,
            MainModule::class,
            MainViewModelModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity

}