package com.example.taskmanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DailyTask::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun DailyTaskDao(): DailyTaskDao
    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,AppDatabase::class.java, "dailyTask")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}