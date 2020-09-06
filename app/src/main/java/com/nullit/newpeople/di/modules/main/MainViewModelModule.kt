package com.nullit.newpeople.di.modules.main

import androidx.lifecycle.ViewModel
import com.nullit.newpeople.di.ViewModelKey
import com.nullit.newpeople.ui.auth.AuthViewModel
import com.nullit.newpeople.ui.main.MainFragmentViewModel
import com.nullit.newpeople.ui.main.photo.SendPhotoViewModel
import com.nullit.newpeople.ui.main.video.SendVideoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SendVideoViewModel::class)
    abstract fun bindSendVideoViewModel(sendVideoViewModel: SendVideoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SendPhotoViewModel::class)
    abstract fun bindSendPhotoViewModel(photoViewModel: SendPhotoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    abstract fun bindMainFragmentViewModel(photoViewModel: MainFragmentViewModel): ViewModel


}