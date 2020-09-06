package com.nullit.newpeople.repo.main

import com.google.gson.JsonObject
import com.nullit.newpeople.api.main.dto.CreateViolationDto
import com.nullit.newpeople.api.main.dto.ViolationData
import com.nullit.newpeople.room.entity.VideoUploaderStateProperty
import com.nullit.newpeople.room.entity.ViolationEntity
import com.nullit.newpeople.util.WrapperResponse
import retrofit2.Response

interface VideoRepo {
    suspend fun createNewViolation(): WrapperResponse<CreateViolationDto>
    suspend fun addNewVideoIntoQueue(path: String, id: Int)
    suspend fun addViolationType(violationId: Int, violationTypeId: Int): WrapperResponse<Response<JsonObject>>
    suspend fun getViolationList(): List<ViolationEntity>
}