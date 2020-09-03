package com.nullit.newpeople.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nullit.newpeople.mappers.UserMapper
import com.nullit.newpeople.repo.auth.AuthRepository
import com.nullit.newpeople.ui.base.BaseViewModel
import com.nullit.newpeople.util.WrapperResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val authRepo: AuthRepository,
    private val userMapper: UserMapper
) : BaseViewModel() {

    private val _successLogin = MutableLiveData<Boolean>()

    val successLogin: LiveData<Boolean>
        get() = _successLogin

    fun login(login: String, password: String) {
        if (login.isBlank() || password.isBlank()) {
            _snackBar.value = "Заполните пустые поля"
            return
        }
        val job = viewModelScope.launch {
            _loading.value = true
            val response = authRepo.attemptLogin(login, password)
            if (response is WrapperResponse.SuccessResponse) {
                val preparedUserProperties =
                    userMapper.fromLoginResponseToUserProperties(response.body)
                val saveResult = authRepo.saveUserDataToDb(preparedUserProperties)
                if (saveResult >= 0) {
                    _successLogin.value = true
                    _loading.value = false
                }
            } else {
                handleErrorResponse(response)
            }

        }
        job.invokeOnCompletion { error ->
            if (error != null) {
                // job completed with error
                _snackBar.value = error.localizedMessage
                _loading.value = false
            }
        }
    }

    fun requestUserAuthStatus() {
        viewModelScope.launch {
            val result = authRepo.checkUserProperties()
            _successLogin.value = result
        }
    }

}