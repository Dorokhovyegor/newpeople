package com.nullit.newpeople.repo

import com.nullit.newpeople.util.WrapperResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

open class JobManager {
    suspend fun <T> safeApiCall(
        context: CoroutineContext,
        apiCall: suspend () -> T
    ): WrapperResponse<T> {
        return withContext(context) {
            try {
                val result = apiCall.invoke()
                WrapperResponse.SuccessResponse(result)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        throwable.printStackTrace()
                        WrapperResponse.GenericError<Nothing>("IOexception")
                    }
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        WrapperResponse.NetworkError<Nothing>(code, errorResponse)
                    }
                    else -> {
                        throwable.printStackTrace()
                        WrapperResponse.GenericError<Nothing>("unknown error")
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): String? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                "Неизвестная ошибка"
            }
        } catch (exception: Exception) {
            null
        }
    }
}