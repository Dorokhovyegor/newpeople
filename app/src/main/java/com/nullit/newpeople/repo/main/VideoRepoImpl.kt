package com.nullit.newpeople.repo.main

import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.api.main.dto.CreateViolationDto
import com.nullit.newpeople.repo.JobManager
import com.nullit.newpeople.room.dao.ProcessDao
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.VideoDao
import com.nullit.newpeople.room.entity.VideoProperty
import com.nullit.newpeople.room.entity.VideoUploaderStateProperty
import com.nullit.newpeople.util.Constants
import com.nullit.newpeople.util.WrapperResponse
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class VideoRepoImpl
@Inject
constructor(
    private val mainApiService: MainApiService,
    private val userDao: UserDao,
    private val videoDao: VideoDao
) : JobManager(), VideoRepo {

    override suspend fun createNewViolation(): WrapperResponse<CreateViolationDto> {
        return safeApiCall(coroutineContext) {
            val info = userDao.requestUserInfo()
            val token = "${info?.token_type} ${info?.token}"
            mainApiService.createViolation(token, "")
        }
    }

    override suspend fun addNewVideoIntoQueue(path: String, id: Int) {
        videoDao.insertVideo(VideoProperty(id, path, false))
    }
}