package com.example.taskmanager.data.database.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.data.database.model.Plan
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {
    @Query("SELECT * FROM `Plan`")
    fun getPlans(): Flow<List<Plan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newPlan: Plan)

    @Delete
    suspend fun delete(newPlan: Plan)

    @Update
    suspend fun edit(newPlan: Plan)
}