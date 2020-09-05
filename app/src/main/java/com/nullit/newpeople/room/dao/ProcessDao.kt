package com.nullit.newpeople.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.nullit.newpeople.room.entity.PhotoUploaderStateProperty
import com.nullit.newpeople.room.entity.VideoUploaderStateProperty

@Dao
interface ProcessDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertVideoStateService(newState: VideoUploaderStateProperty): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertPhotoStateService(newState: PhotoUploaderStateProperty): Long

    @Query("SELECT * from video_process_state")
    suspend fun getVideoUploaderState(): VideoUploaderStateProperty

    @Query("SELECT * FROM photo_process_state")
    suspend fun getPhotoUploaderState(): PhotoUploaderStateProperty
}