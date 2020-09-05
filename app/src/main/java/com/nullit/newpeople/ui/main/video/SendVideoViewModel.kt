package com.nullit.newpeople.ui.main.video


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nullit.newpeople.repo.main.VideoRepo
import com.nullit.newpeople.ui.base.BaseViewModel
import com.nullit.newpeople.util.WrapperResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class SendVideoViewModel
@Inject
constructor(
    private val videoRepo: VideoRepo
) : BaseViewModel() {

    val videoUri = MutableLiveData<Uri>()
    val lastViolationId = MutableLiveData<Int>()
    val currentVideoState = MutableLiveData<String>()

    fun createNewViolation() {
        viewModelScope.launch {
            val result = videoRepo.createNewViolation()
            if (result is WrapperResponse.SuccessResponse) {
                lastViolationId.value = result.body.data.violation.violationId
            } else {
                handleErrorResponse(result)
            }
        }
    }

    suspend fun addNewVideoIntoQueue(path: String, id: Int) {
        videoRepo.addNewVideoIntoQueeu(path, id)
    }

    suspend fun checkStateProcess(): String {
        return videoRepo.checkStateProcess()
    }
}