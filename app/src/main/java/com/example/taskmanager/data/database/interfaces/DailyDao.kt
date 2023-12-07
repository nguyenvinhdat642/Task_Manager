package com.example.taskmanager.data.database.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.data.database.model.Daily

@Dao
interface DailyDao {
    @Query("SELECT * FROM `Daily`")
    fun getAll(): List<Daily>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newDaily: Daily)

    @Delete
    fun delete(newDaily: Daily)

    @Update
    fun edit(newDaily: Daily)
}