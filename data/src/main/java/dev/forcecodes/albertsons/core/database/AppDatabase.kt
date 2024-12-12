package dev.forcecodes.albertsons.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcecodes.albertsons.core.di.EntityConverters
import dev.forcecodes.albertsons.core.local.UserInfoEntity

@Database(
    entities = [UserInfoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(EntityConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}
