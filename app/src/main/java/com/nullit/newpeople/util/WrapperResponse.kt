package com.nullit.newpeople.util

import retrofit2.Response

sealed class WrapperResponse<out T> {
    companion object {
        private val TAG: String = "AppDebug"


        fun <T> create(error: Throwable): GenericError<T> {
            return GenericError(
                error.message ?: "unknown error"
            )
        }

        fun <T> create(response: Response<T>): WrapperResponse<T> {
            if (response.isSuccessful) {
                val body = response.body()
                return if (body == null || response.code() == 204) {
                    return NetworkError(
                        response.code(),
                        null
                    )
                } else if (response.code() == 401) {
                    NetworkError(
                        401,
                        "unauthorized"
                    )
                } else {
                    SuccessResponse(body = body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                return GenericError<Nothing>(
                    errorMsg ?: "unknown error"
                )
            }
        }
    }

    data class SuccessResponse<T>(val body: T) : WrapperResponse<T>()
    data class NetworkError<T>(val code: Int?, val networkError: String?) : WrapperResponse<T>()
    data class GenericError<T>(val localError: String?) : WrapperResponse<T>()
}