package com.nullit.newpeople.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.PropertyKey

@Entity(tableName = "video_process_state")
data class VideoUploaderStateProperty(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name= "process_state")
    val processState: String
)

