package com.example.taskmanager

import androidx.room.TypeConverter
import java.util.Date
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): Date? {
        return if (value == null) null else SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(value)
            ?.let { Date(it.time) }
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) }
    }
}
