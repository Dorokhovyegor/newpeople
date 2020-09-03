package com.nullit.newpeople.di.modules.main

import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.di.modules.common.ViewModelFactoryModule
import com.nullit.newpeople.ui.main.MainFragment
import com.nullit.newpeople.ui.main.photo.SendFormFragment
import com.nullit.newpeople.ui.main.video.SendVideoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun bindMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun bindSendVideoFragment(): SendVideoFragment

    @ContributesAndroidInjector
    abstract fun bindSendFormFragment(): SendFormFragment
}