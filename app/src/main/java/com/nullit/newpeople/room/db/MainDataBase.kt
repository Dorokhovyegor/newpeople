package com.nullit.newpeople.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.entity.UserProperties

@Database(version = 1, entities = [UserProperties::class])
abstract class MainDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        const val DB_NAME = "np_db"
    }

}