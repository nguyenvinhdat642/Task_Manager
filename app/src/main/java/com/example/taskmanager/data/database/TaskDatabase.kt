package com.example.taskmanager.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.data.database.interfaces.DailyDao
import com.example.taskmanager.data.database.interfaces.PlanDao
import com.example.taskmanager.data.database.model.Daily
import com.example.taskmanager.data.database.model.Plan

@Database(entities = [Plan::class, Daily::class], version= 1)
abstract class TaskDatabase: RoomDatabase(){
    abstract fun planDao(): PlanDao
    abstract fun dailyDao(): DailyDao
}