package com.nullit.newpeople.mappers

import com.nullit.newpeople.api.main.dto.LoginDto
import com.nullit.newpeople.room.entity.ViolationEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViolationMapper
@Inject
constructor() {

    fun fromLoginResponseToViolationList(loginResponse: LoginDto): List<ViolationEntity> {
        val violations = ArrayList<ViolationEntity>()
        loginResponse.data?.violationList?.forEach {
            violations.add(ViolationEntity(it.typeId, it.name))
        }
        return violations
    }
}