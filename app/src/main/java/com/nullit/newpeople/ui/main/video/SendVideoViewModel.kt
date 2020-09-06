package com.nullit.newpeople.ui.main.video


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nullit.newpeople.repo.main.VideoRepo
import com.nullit.newpeople.room.entity.ViolationEntity
import com.nullit.newpeople.ui.base.BaseViewModel
import com.nullit.newpeople.util.WrapperResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendVideoViewModel
@Inject
constructor(
    private val videoRepo: VideoRepo
) : BaseViewModel() {

    val videoUri = MutableLiveData<Uri>()
    val lastViolationId = MutableLiveData<Int>()
    val violationList = MutableLiveData<List<ViolationEntity>>()

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

    fun getViolationList() {
        viewModelScope.launch {
            violationList.value = videoRepo.getViolationList()
        }
    }

    suspend fun addNewVideoIntoQueue(path: String, id: Int) {
        videoRepo.addNewVideoIntoQueue(path, id)
    }

    suspend fun addCategory(violationId: Int, typeId: Int) {
        GlobalScope.launch(IO) {
            val result = videoRepo.addViolationType(violationId, typeId)
            if (result is WrapperResponse.SuccessResponse) {
                withContext(Main) {
                    _snackBar.value = "Тип нарушения отправлен"
                }
            } else {
                handleErrorResponse(result)
            }
        }
    }

}