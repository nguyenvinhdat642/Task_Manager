package com.example.taskmanager.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

sealed class Task
@Entity
data class Plan(
    @PrimaryKey val id: Int,
    val title: String,
    val category: String,
    val description: String,
    val startDate: Date,
    val endDate: Date,
    val todoList: ArrayList<String>
) : Task()

@Entity
data class Daily(
    @PrimaryKey val id: Int,
    val title: String,
    val category: String,
    val description: String,
    val startDate: Date,
    val endDate: Date,
): Task()

