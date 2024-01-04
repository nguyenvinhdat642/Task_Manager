package com.example.taskmanager.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity
data class Plan(
    @PrimaryKey(autoGenerate = true) val planId: Long = 0,
    @ColumnInfo(name = "start_date") val startDate: Date,
    @ColumnInfo(name = "end_date") val endDate: Date,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "state") val state: Boolean,
    @ColumnInfo(name = "progress") val progress: Int
)
@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val todoId: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "state") val state: Boolean,
    val planCreate: Long? = null
)
data class PlanWithToDoLists(
    @Embedded val plan: Plan,
    @Relation(
        parentColumn = "planId",
        entityColumn = "planCreate"
    )
    val todo: List<Todo>
)