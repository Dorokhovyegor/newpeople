package com.nullit.newpeople.ui.main

import androidx.lifecycle.MutableLiveData
import com.nullit.newpeople.repo.auth.AuthRepository
import com.nullit.newpeople.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragmentViewModel
@Inject
constructor(
   private val authRepository: AuthRepository
) : BaseViewModel() {

    val logOutSuccess =  MutableLiveData<Boolean>()

    fun logout() {
        GlobalScope.launch(Dispatchers.IO) {
            if (authRepository.logOut()) {
                logOutSuccess.postValue(true)
            }
        }
    }

}