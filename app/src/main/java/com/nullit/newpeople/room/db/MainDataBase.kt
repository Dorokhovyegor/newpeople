package com.nullit.newpeople.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nullit.newpeople.room.dao.ProcessDao
import com.nullit.newpeople.room.dao.UserDao
import com.nullit.newpeople.room.dao.VideoDao
import com.nullit.newpeople.room.entity.*

@Database(
    version = 1,
    entities = [UserProperties::class, VideoProperty::class, VideoUploaderStateProperty::class, PhotoProperty::class, PhotoUploaderStateProperty::class]
)
abstract class MainDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getVideoDao(): VideoDao

    abstract fun getProccessDao(): ProcessDao

    companion object {
        const val DB_NAME = "np_db"
    }

}