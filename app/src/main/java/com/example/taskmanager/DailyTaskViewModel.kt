package com.example.taskmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyTaskViewModel(application: Application): AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dailyTaskDao = db.DailyTaskDao()

    fun addDailyTask(dailyTask: DailyTask){
        viewModelScope.launch(Dispatchers.IO){
            dailyTaskDao.insertAll(dailyTask)
        }
    }
    fun getAllDailyTasks(): List<DailyTask>{
        val sampleList = arrayListOf<DailyTask>()
        viewModelScope.launch(Dispatchers.IO){
            sampleList.clear()
            sampleList.addAll(dailyTaskDao.getAll())
        }
        return sampleList
    }
}