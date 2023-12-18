package com.example.taskmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyTaskViewModel(application: Application): AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dailyTaskDao = db.DailyTaskDao()

    fun addSampleDailyTask(){
        val newDailyTask = DailyTask(startDate = "12/03/2002", endDate = "15/03/2022", title = "deadline", content = "Hao doing deadline")
        viewModelScope.launch(Dispatchers.IO){
            dailyTaskDao.insertAll(newDailyTask)
        }
    }

}