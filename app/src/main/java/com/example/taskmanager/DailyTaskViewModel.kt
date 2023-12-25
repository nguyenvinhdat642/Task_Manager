package com.example.taskmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dailyTaskDao = db.DailyTaskDao()
    val selectedDailyTask = MutableLiveData<DailyTask>()

    fun addDailyTask(dailyTask: DailyTask) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyTaskDao.insertAll(dailyTask)
        }
    }

    fun getAllDailyTasks(): List<DailyTask> {
        val sampleList = arrayListOf<DailyTask>()
        viewModelScope.launch(Dispatchers.IO) {
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

    fun selectedDailyTask(clickedTask: DailyTask) {
        selectedDailyTask.value = clickedTask
    }

    fun parseToDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.parse(dateString) ?: Date()
    }

    fun parseToString(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun finishTask(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyTaskDao.updateTaskState(taskId, newState = true)
        }
    }

    fun unfinishTask(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyTaskDao.updateTaskState(taskId, newState = false)
        }
    }

    fun deleteTask(dailyTask: DailyTask) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyTaskDao.delete(dailyTask)
        }
    }

    fun updateTaskById(
        taskId: Int,
        startDate: Date,
        endDate: Date,
        title: String,
        content: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyTaskDao.updateTaskById(taskId, startDate, endDate, title, content)
        }
    }
}