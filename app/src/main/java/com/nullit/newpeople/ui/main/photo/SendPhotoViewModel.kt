package com.nullit.newpeople.ui.main.photo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nullit.newpeople.repo.main.PhotoRepo
import com.nullit.newpeople.room.entity.ViolationEntity
import com.nullit.newpeople.ui.base.BaseViewModel
import com.nullit.newpeople.ui.main.photo.adapter.PhotoPresentationModel
import com.nullit.newpeople.util.WrapperResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendPhotoViewModel
@Inject
constructor(
    private val photoRepo: PhotoRepo
) : BaseViewModel() {

    val photos = MutableLiveData<List<PhotoPresentationModel>>()
    val lastViolationId = MutableLiveData<Int>()
    val violationList = MutableLiveData<List<ViolationEntity>>()

    suspend fun createNewViolation(): Int{
        val result = photoRepo.createNewViolation()
        if (result is WrapperResponse.SuccessResponse) {
            lastViolationId.value = result.body.data.violation.violationId
            return result.body.data.violation.violationId
        } else {
            handleErrorResponse(result)
            return -1
        }
    }

    fun getViolationList() {
        viewModelScope.launch {
            violationList.value = photoRepo.getViolationList()
        }
    }

    suspend fun addCategory(violationId: Int, typeId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = photoRepo.addViolationType(violationId, typeId)
            if (result is WrapperResponse.SuccessResponse) {
                withContext(Dispatchers.Main) {
                    _snackBar.value = "Тип нарушения отправлен"
                }
            } else {
                handleErrorResponse(result)
            }
        }
    }

}