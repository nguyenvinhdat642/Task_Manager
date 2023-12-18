package com.example.taskmanager.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.example.taskmanager.DailyTask
import com.example.taskmanager.DailyTaskViewModel
import com.example.taskmanager.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskFragment : Fragment() {
    private val viewModel: DailyTaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)
        val tvStart = view.findViewById<AutoCompleteTextView>(R.id.tvStart)
        val tvEnd = view.findViewById<AutoCompleteTextView>(R.id.tvEnd)
        val edtTitle = view.findViewById<TextInputEditText>(R.id.edtTitle)
        val edtDes = view.findViewById<EditText>(R.id.edtDes)
        val btnCreate = view.findViewById<MaterialButton>(R.id.btnCreateTask)
        tvStart.datePicker()
        tvEnd.datePicker()
        btnCreate.setOnClickListener {
            val newDailyTask = DailyTask(
                startDate = tvStart.text.toString(),
                endDate = tvEnd.text.toString(),
                title = edtTitle.text.toString(),
                content = edtDes.text.toString()
                )
            viewModel.addDailyTask(newDailyTask)
            findNavController().navigate(R.id.action_addTaskFragment_to_homeFragment)
        }
        return view
    }
    private fun AutoCompleteTextView.datePicker(){
        this.setOnClickListener {
            val currentDate = Calendar.getInstance()
            var mYear = currentDate.get(Calendar.YEAR)
            var mMonth = currentDate.get(Calendar.MONTH)
            var mDay = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = context?.let { it1 ->
                DatePickerDialog(it1,
                    { view, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val format = "dd/MM/yyyy"
                        val simpleDateFormat = SimpleDateFormat(format, Locale.JAPAN)
                        this.setText(simpleDateFormat.format(calendar.time))
                        mDay = dayOfMonth
                        mMonth = month
                        mYear = year
                    }, mYear, mMonth, mDay)
            }
            datePickerDialog?.show()
        }
    }
}