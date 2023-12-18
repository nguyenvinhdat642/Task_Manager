package com.example.taskmanager

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DailyTaskDao {
    @Query("SELECT * FROM dailyTask")
    fun getAll(): List<DailyTask>

    @Query("SELECT * FROM dailyTask WHERE id IN (:dailyTaskIds)")
    fun loadAllByIds(dailyTaskIds: IntArray): List<DailyTask>

    @Query("SELECT * FROM dailyTask WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): DailyTask

    @Insert
    fun insertAll(vararg dailyTasks: DailyTask)

    @Delete
    fun delete(dailyTask: DailyTask)
}