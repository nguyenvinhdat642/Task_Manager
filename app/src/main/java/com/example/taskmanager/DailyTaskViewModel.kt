package com.example.taskmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

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
    fun getDailyTaskBetweenDay(selectedDate: Date): List<DailyTask> {
        val result = arrayListOf<DailyTask>()
        viewModelScope.launch(Dispatchers.IO) {
            result.clear()
            result.addAll(dailyTaskDao.getTasksBetweenDates(selectedDate))
        }
        return result
    }
}