package com.nullit.newpeople.api.main.dto

import com.google.gson.annotations.SerializedName

data class CreateViolationDto(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: ViolationInfo
)

data class ViolationInfo(
    @SerializedName("violation") val violation: ViolationBody
)

data class ViolationBody(
    @SerializedName("id") val violationId: Int
)