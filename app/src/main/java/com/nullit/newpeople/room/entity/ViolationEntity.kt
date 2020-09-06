package com.nullit.newpeople.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "violations")
data class ViolationEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val violationId: Int,
    @ColumnInfo(name = "name")
    val name: String
)