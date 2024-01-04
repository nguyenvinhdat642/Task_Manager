package com.example.taskmanager.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.taskmanager.model.DailyTask
import java.util.Date

@Dao
interface DailyTaskDao {
    @Query("SELECT * FROM dailyTask")
    fun getAll(): List<DailyTask>

    @Query("SELECT * FROM dailyTask WHERE start_date LIKE :startDate AND end_date LIKE :endDate")
    fun getTasksBetweenDates(startDate: Date, endDate: Date): List<DailyTask>

    @Query("SELECT * FROM dailytask WHERE start_date <= :selectedDate AND end_date >= :selectedDate")
    fun getAllTaskOfTheDate(selectedDate: Date): List<DailyTask>
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertTask(dailyTask: DailyTask)

    @Query("SELECT * FROM dailyTask WHERE taskId IN (:dailyTaskIds)")
    fun loadAllByIds(dailyTaskIds: IntArray): List<DailyTask>

    @Query("SELECT * FROM dailyTask WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): DailyTask

    @Insert
    fun insertAll(vararg dailyTasks: DailyTask)

    @Delete
    fun delete(dailyTask: DailyTask)

    @Query("UPDATE DailyTask SET state = :newState WHERE taskId = :taskId")
    suspend fun updateTaskState(taskId: kotlin.Long, newState: Boolean)

    @Query("UPDATE DailyTask SET start_date = :startDate, end_date = :endDate, title = :title, content = :content WHERE taskId = :taskId")
    suspend fun updateTaskById(
        taskId: kotlin.Long,
        startDate: Date,
        endDate: Date,
        title: String,
        content: String
    )
}