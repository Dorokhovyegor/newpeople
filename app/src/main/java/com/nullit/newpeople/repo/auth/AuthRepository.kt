package com.nullit.newpeople.repo.auth

import com.nullit.newpeople.api.main.dto.LoginDto
import com.nullit.newpeople.room.entity.UserProperties
import com.nullit.newpeople.util.WrapperResponse

interface AuthRepository {
    suspend fun attemptLogin(login: String, password: String): WrapperResponse<LoginDto>
    suspend fun saveUserDataToDb(user: UserProperties): Long // int - result
    suspend fun checkUserProperties(): Boolean
}