package com.nullit.newpeople.room.dao

import androidx.room.*
import com.nullit.newpeople.room.entity.VideoProperty
import com.nullit.newpeople.room.entity.ViolationEntity

@Dao
interface ViolationDao {
    @Transaction
    @Query("SELECT * From violations")
    suspend fun getAllViolations(): List<ViolationEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(violations: List<ViolationEntity>)
    @Query("DELETE from videos")
    suspend fun deleteAll(): Int
}