package com.nullit.newpeople.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_property")
data class UserProperties(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "fio")
    val name: String,
    @ColumnInfo(name = "token_type")
    val token_type: String,
    @ColumnInfo(name = "token")
    val token: String
)