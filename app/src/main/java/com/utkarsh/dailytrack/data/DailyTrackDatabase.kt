package com.utkarsh.dailytrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class, HabitEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DailyTrackDatabase : RoomDatabase() {
    abstract fun dailyTrackDao(): DailyTrackDao

    companion object {
        @Volatile private var INSTANCE: DailyTrackDatabase? = null

        fun getInstance(context: Context): DailyTrackDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DailyTrackDatabase::class.java,
                    "dailytrack.db"
                ).build().also { INSTANCE = it }
            }
    }
}
