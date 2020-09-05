package com.nullit.newpeople.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoProperty(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "violation_id")
    val violationId: Int,
    @ColumnInfo(name = "photo_path")
    val photoPath: String,
    @ColumnInfo(name = "uploaded")
    val uploaded: Boolean
)