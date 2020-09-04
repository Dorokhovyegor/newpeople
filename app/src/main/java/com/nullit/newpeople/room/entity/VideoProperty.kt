package com.nullit.newpeople.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoProperty(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "violation_id")
    val violationId: Int,
    @ColumnInfo(name = "video_path")
    val videoPath: String,
    @ColumnInfo(name = "uploaded")
    val uploaded: Boolean
)