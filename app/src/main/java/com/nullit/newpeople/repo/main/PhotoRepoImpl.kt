package com.nullit.newpeople.repo.main

import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.api.main.dto.CreateViolationDto
import com.nullit.newpeople.repo.JobManager
import com.nullit.newpeople.room.dao.PhotoDao
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.VideoDao
import com.nullit.newpeople.util.WrapperResponse
import kotlinx.coroutines.currentCoroutineContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class PhotoRepoImpl
@Inject
constructor(
    private val mainApiService: MainApiService,
    private val userDao: UserDao,
    private val photoDao: PhotoDao
) : JobManager(), PhotoRepo {

    override suspend fun createNewViolationSendPhoto(): WrapperResponse<CreateViolationDto> {
        return safeApiCall(coroutineContext) {
            val info = userDao.requestUserInfo()
            val token = "${info?.token_type} ${info?.token}"
            mainApiService.createViolation(token, "")
        }
    }

    override suspend fun addNewPhotosIntoQueue(paths: List<String>, id: Int) {

    }
}