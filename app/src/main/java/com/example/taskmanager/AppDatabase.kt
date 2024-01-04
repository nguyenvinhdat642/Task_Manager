package com.example.taskmanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmanager.interfaces.DailyTaskDao
import com.example.taskmanager.interfaces.PlanDao
import com.example.taskmanager.model.DailyTask
import com.example.taskmanager.model.Plan
import com.example.taskmanager.model.PlanWithToDoLists
import com.example.taskmanager.model.TodoList

@Database(entities = [DailyTask::class, Plan::class, TodoList::class], version = 5)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun DailyTaskDao(): DailyTaskDao
    abstract fun PlanDao(): PlanDao
    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,AppDatabase::class.java, "dailyTask")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}