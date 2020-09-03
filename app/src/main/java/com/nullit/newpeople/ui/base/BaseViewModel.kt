package com.nullit.newpeople.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullit.newpeople.util.WrapperResponse
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    protected val _endSession = MutableLiveData<Boolean>()
    val endSession: LiveData<Boolean>
        get() = _endSession

    protected val _snackBar = MutableLiveData<String?>()
    val snackBar: LiveData<String?>
        get() = _snackBar

    protected val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val progressBar: LiveData<Boolean>
        get() = _loading

    protected fun handleErrorResponse(response: WrapperResponse<*>) {
        when (response) {
            is WrapperResponse.GenericError -> {
                _snackBar.value = response.localError
            }
            // only for non auth fragment and activities
            is WrapperResponse.NetworkError -> {
                if (response.code == 401) {
                    _endSession.value = true
                }
                _snackBar.value = response.networkError
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun onSnackBarShown() {
        _snackBar.value = null
    }

}