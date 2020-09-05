package com.nullit.newpeople.room.dao

import androidx.room.*
import com.nullit.newpeople.room.entity.UserProperties
import com.nullit.newpeople.room.entity.VideoProperty

@Dao
interface VideoDao {
    @Query("SELECT * From videos WHERE uploaded = 0 order by violation_id asc")
    suspend fun getDoesNotUploadedVideo(): List<VideoProperty>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoProperty): Long
    @Query("UPDATE videos SET uploaded =:uploaded WHERE violation_id =:id")
    suspend fun updateVideo(uploaded: Boolean, id: Int): Int
    @Query("DELETE from videos")
    suspend fun deleteUser(): Int
}