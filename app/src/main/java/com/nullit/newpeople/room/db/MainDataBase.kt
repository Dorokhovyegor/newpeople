package com.nullit.newpeople.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.VideoDao
import com.nullit.newpeople.room.entity.UserProperties
import com.nullit.newpeople.room.entity.VideoProperty

@Database(version = 1, entities = [UserProperties::class, VideoProperty::class])
abstract class MainDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getVideoDao(): VideoDao

    companion object {
        const val DB_NAME = "np_db"
    }

}