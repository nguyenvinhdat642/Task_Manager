package com.example.taskmanager.data.database.repository


import com.example.taskmanager.data.database.model.Plan
import kotlinx.coroutines.flow.Flow

interface PlanRepository {
    fun getPlans(): Flow<List<Plan>>


    suspend fun insert(newPlan: Plan)


    suspend fun delete(newPlan: Plan)


    suspend fun edit(newPlan: Plan)
}