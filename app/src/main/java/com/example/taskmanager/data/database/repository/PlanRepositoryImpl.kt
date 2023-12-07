package com.example.taskmanager.data.database.repository

import com.example.taskmanager.data.database.interfaces.PlanDao
import com.example.taskmanager.data.database.model.Plan
import kotlinx.coroutines.flow.Flow

class PlanRepositoryImpl(
    private val planDao: PlanDao
) : PlanRepository {
    override fun getPlans(): Flow<List<Plan>> {
        return planDao.getPlans()
    }

    override suspend fun insert(newPlan: Plan) {
        planDao.insert(newPlan)
    }

    override suspend fun delete(newPlan: Plan) {
        planDao.delete(newPlan)
    }

    override suspend fun edit(newPlan: Plan) {
        planDao.edit(newPlan)
    }
}