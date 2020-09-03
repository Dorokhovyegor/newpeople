package com.nullit.newpeople.di.modules.auth

import androidx.lifecycle.ViewModel
import com.nullit.newpeople.di.ViewModelKey
import com.nullit.newpeople.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

}