package com.nullit.newpeople.repo.main

import com.nullit.newpeople.api.main.dto.CreateViolationDto
import com.nullit.newpeople.util.WrapperResponse

interface PhotoRepo {
    suspend fun createNewViolation(): WrapperResponse<CreateViolationDto>
    suspend fun addNewPhotosIntoQueue(paths: List<String>, id: Int)
}
