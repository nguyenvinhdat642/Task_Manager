package com.example.taskmanager

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.example.taskmanager.ui.SettingFragment

class SharedPreferencesManager(context: Context) {
    private val preferences = context.getSharedPreferences(
        context.packageName,
        MODE_PRIVATE
    )
    private val editor = preferences.edit()

    private val keyTheme = "theme"

    var theme
        get() = preferences.getInt(keyTheme, 2)
        set(value) {
            editor.putInt(keyTheme, value)
            editor.commit()
        }

    val themeFlag = arrayOf(
        AppCompatDelegate.MODE_NIGHT_NO,
        AppCompatDelegate.MODE_NIGHT_YES,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
}