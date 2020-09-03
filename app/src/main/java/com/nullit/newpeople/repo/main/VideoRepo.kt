package com.nullit.newpeople.repo.main

import com.nullit.newpeople.api.main.dto.CreateViolationDto
import com.nullit.newpeople.util.WrapperResponse

interface VideoRepo {
    suspend fun createNewViolation(): WrapperResponse<CreateViolationDto>
}