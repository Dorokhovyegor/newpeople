package com.nullit.newpeople.repo.auth

import com.nullit.newpeople.api.main.MainApiService
import com.nullit.newpeople.api.main.dto.LoginDto
import com.nullit.newpeople.repo.JobManager
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.ViolationDao
import com.nullit.newpeople.room.entity.UserProperties
import com.nullit.newpeople.room.entity.ViolationEntity
import com.nullit.newpeople.util.WrapperResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AuthRepositoryImpl
@Inject constructor(
    private val authApiService: MainApiService,
    private val userDao: UserDao,
    private val violationDao: ViolationDao
) : JobManager(), AuthRepository {

    override suspend fun attemptLogin(login: String, password: String): WrapperResponse<LoginDto> {
        return safeApiCall(Dispatchers.IO) {
            authApiService.login(login, password)
        }
    }

    override suspend fun saveAllViolations(list: List<ViolationEntity>) {
        violationDao.insertAll(list)
    }

    override suspend fun logOut(): Boolean {
        if (userDao.deleteUser() >= 0) {
            return true
        }
        return false
    }

    override suspend fun saveUserDataToDb(user: UserProperties): Long {
        return userDao.insertUser(user)
    }

    override suspend fun checkUserProperties(): Boolean {
        val userInfo = userDao.requestUserInfo()
        return userInfo != null && userInfo.token.isNotEmpty()
    }

}