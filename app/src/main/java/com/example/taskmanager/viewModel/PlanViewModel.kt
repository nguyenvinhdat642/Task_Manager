package com.example.taskmanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.AppDatabase
import com.example.taskmanager.model.Plan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlanViewModel(application: Application): AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val planDao = db.PlanDao()

    fun getAllPlans(): List<Plan> {
        val sampleList = arrayListOf<Plan>()
        viewModelScope.launch(Dispatchers.IO) {
            sampleList.clear()
            sampleList.addAll(planDao.getAll())
        }
        return sampleList
    }
}