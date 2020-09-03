package com.nullit.newpeople.di.modules.main

import androidx.lifecycle.ViewModel
import com.nullit.newpeople.di.ViewModelKey
import com.nullit.newpeople.ui.auth.AuthViewModel
import com.nullit.newpeople.ui.main.video.SendVideoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SendVideoViewModel::class)
    abstract fun bindAuthViewModel(sendVideoViewModel: SendVideoViewModel): ViewModel

}