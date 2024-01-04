package com.example.taskmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.taskmanager.R
import com.example.taskmanager.SharedPreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingFragment : Fragment() {
    private lateinit var btnTheme: Button
    private lateinit var tvTheme: TextView
    private val themeTitle = arrayOf("Light", "Dark", "Auto")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        val SharedPreferencesManager = SharedPreferencesManager(requireContext())
        var checkTheme = SharedPreferencesManager.theme

        btnTheme = view.findViewById(R.id.btnTheme)
        tvTheme = view.findViewById(R.id.tvTheme)

        tvTheme.text = themeTitle[SharedPreferencesManager.theme]

        val themeDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Theme")
            .setPositiveButton("OK") { _, _ ->
                SharedPreferencesManager.theme = checkTheme
                AppCompatDelegate.setDefaultNightMode(SharedPreferencesManager.themeFlag[checkTheme])
            }
            .setSingleChoiceItems(themeTitle, checkTheme){_,which ->
                checkTheme = which
            }
            .setCancelable(false)


        btnTheme.setOnClickListener {
            themeDialog.show()
        }
        return view
    }

}