package com.example.taskmanager.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.taskmanager.SharedPreferencesManager

class ChangeTheme: Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPreferencesManager = SharedPreferencesManager(this)
        AppCompatDelegate.setDefaultNightMode(sharedPreferencesManager.themeFlag[sharedPreferencesManager.theme])
    }
}