package com.nullit.newpeople.room.dao

import androidx.room.*
import com.nullit.newpeople.room.entity.UserProperties

@Dao
interface UserDao {
    @Query("SELECT * From user_property")
    suspend fun requestUserInfo(): UserProperties?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProperties): Long
    @Update
    suspend fun updateUser(user: UserProperties): Int
    @Query("DELETE from user_property")
    suspend fun deleteUser(): Int
}