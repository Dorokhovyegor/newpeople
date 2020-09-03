package com.nullit.newpeople.mappers

import com.nullit.newpeople.api.main.dto.LoginDto
import com.nullit.newpeople.room.entity.UserProperties
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper
@Inject
constructor() {

    fun fromLoginResponseToUserProperties(loginResponse: LoginDto): UserProperties {
        return UserProperties(
            loginResponse.data?.user?.userId!!,
            loginResponse.data.user.fio,
            loginResponse.data.tokenType ,
            loginResponse.data.token
        )
    }
}