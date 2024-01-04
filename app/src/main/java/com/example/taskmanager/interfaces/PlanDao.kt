package com.example.taskmanager.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.taskmanager.model.Plan
import com.example.taskmanager.model.PlanWithToDoLists
import com.example.taskmanager.model.Todo
import java.util.Date

@Dao
interface PlanDao {
    @Query("SELECT * FROM `plan`")
    fun getAll(): List<Plan>

    @Query("SELECT * FROM `plan` WHERE start_date LIKE :startDate AND end_date LIKE :endDate")
    fun getPlansBetweenDates(startDate: Date, endDate: Date): List<Plan>

    @Query("SELECT * FROM `plan` WHERE start_date <= :selectedDate AND end_date >= :selectedDate")
    fun getAllPlansOfTheDate(selectedDate: Date): List<Plan>

    @Transaction
    @Query("SELECT * FROM `plan`")
    fun getPlansWithTodolist(): List<PlanWithToDoLists>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlan(plan: Plan): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTodoList(todo: Todo)
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertTask(dailyTask: Plan)
    @Transaction
    fun insertPlansWithTodolist(plan: Plan, todo: List<Todo>){
        val planId = insertPlan(plan)
        val todolistWithUserId = todo.map { it.copy(planCreate = planId) }
        todolistWithUserId.forEach { insertTodoList(it) }
    }

    @Query("SELECT * FROM `plan` WHERE planId IN (:planIds)")
    fun loadAllByIds(planIds: IntArray): List<Plan>

    @Query("SELECT * FROM `plan` WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): Plan

    @Insert
    fun insertAll(vararg plans: Plan)

    @Delete
    fun delete(plan: Plan)

    @Query("UPDATE `plan` SET state = :newState WHERE planId = :taskId")
    suspend fun updatePlanState(taskId: Long, newState: Boolean)

    @Query("UPDATE `plan` SET start_date = :startDate, end_date = :endDate, title = :title," +
            " content = :content, progress = :progress WHERE planId = :planId")
    suspend fun updatePlanById(
        planId: Long,
        startDate: Date,
        endDate: Date,
        title: String,
        content: String,
        progress: Int
    )
}