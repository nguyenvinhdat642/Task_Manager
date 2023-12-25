package com.example.taskmanager

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Query("UPDATE DailyTask SET state = :newState WHERE id = :taskId")
    suspend fun updateTaskState(taskId: Int, newState: Boolean)

    @Query("UPDATE DailyTask SET start_date = :startDate, end_date = :endDate, title = :title, content = :content WHERE id = :taskId")
    suspend fun updateTaskById(
        taskId: Int,
        startDate: Date,
        endDate: Date,
        title: String,
        content: String
    )
}