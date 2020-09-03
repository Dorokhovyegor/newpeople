package com.nullit.newpeople.di.modules.common

import androidx.lifecycle.ViewModelProvider
import com.nullit.newpeople.util.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

}