package com.nullit.newpeople.api.main.dto

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: LoginData?
)

data class LoginData(
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserData,
    @SerializedName("violation_types") val violationList: List<ViolationData>
)

data class UserData(
    @SerializedName("id") val userId: Int,
    @SerializedName("fio") val fio: String
)

data class ViolationData(
    @SerializedName("id") val violationId: Int,
    @SerializedName("name") val name: String
)