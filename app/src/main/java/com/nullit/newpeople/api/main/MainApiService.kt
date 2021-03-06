package com.nullit.newpeople.api.main

import com.google.gson.JsonObject
import com.nullit.newpeople.api.main.dto.CreateViolationDto
import com.nullit.newpeople.api.main.dto.LoginDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MainApiService {

    @Multipart
    @POST("/api/v1/login")
    suspend fun login(
        @Part("login") login: String,
        @Part("password") password: String
    ): LoginDto

    @Multipart
    @POST("/api/v1/violations")
    suspend fun createViolation(
        @Header("Authorization") token: String,
        @Part("description") description: String
    ): CreateViolationDto

    @Streaming
    @Multipart
    @POST("/api/v1/violations/{id}")
    suspend fun addVideo(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("_method") description: String = "patch",
        @Part("action") action: String = "add-video",
        @Part video: MultipartBody.Part
    ): Response<JsonObject>

    @Multipart
    @POST("/api/v1/violations/{id}")
    suspend fun addType(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("_method") description: String = "patch",
        @Part("action") action: String = "set-type",
        @Part("type_id") typeId: Int
    ): Response<JsonObject>

    @Streaming
    @Multipart
    @POST("/api/v1/violations/{id}")
    suspend fun addPhotos(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("_method") description: String = "patch",
        @Part("action") action: String = "add-photo",
        @Part photos: List<MultipartBody.Part>
    ): Response<JsonObject>
}