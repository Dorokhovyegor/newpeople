package com.nullit.newpeople.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nullit.newpeople.room.entity.PhotoProperty
import com.nullit.newpeople.room.entity.VideoProperty

@Dao
interface PhotoDao {
    @Query("SELECT * From photos WHERE uploaded = 0 order by violation_id asc")
    suspend fun getDoesNotUploadedPhotos(): List<PhotoProperty>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(photo: PhotoProperty): Long
    @Query("UPDATE photos SET uploaded =:uploaded WHERE violation_id =:id")
    suspend fun updatePhoto(uploaded: Boolean, id: Int): Int
    @Query("DELETE from photos")
    suspend fun deleteAllPhotos(): Int
}