package com.example.taskmanager

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface DailyTaskDao {
    @Query("SELECT * FROM dailyTask")
    fun getAll(): List<DailyTask>

    @Query("SELECT * FROM dailyTask WHERE start_date LIKE :selectedDate")
    fun getTasksBetweenDates(selectedDate: Date): List<DailyTask>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertTask(dailyTask: DailyTask)

    @Query("SELECT * FROM dailyTask WHERE id IN (:dailyTaskIds)")
    fun loadAllByIds(dailyTaskIds: IntArray): List<DailyTask>

    @Query("SELECT * FROM dailyTask WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): DailyTask

    @Insert
    fun insertAll(vararg dailyTasks: DailyTask)

    @Delete
    fun delete(dailyTask: DailyTask)
}