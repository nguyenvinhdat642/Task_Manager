package com.example.taskmanager.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.example.taskmanager.DailyTask
import com.example.taskmanager.DailyTaskViewModel
import com.example.taskmanager.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskFragment : Fragment() {

    private val viewModel: DailyTaskViewModel by activityViewModels()

    private lateinit var tvStart: AutoCompleteTextView
    private lateinit var tvEnd: AutoCompleteTextView
    private lateinit var edtTitle: TextInputEditText
    private lateinit var edtDes: EditText
    private lateinit var btnCreate: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)
        initializeViews(view)
        setDefaultDates()
        setClickListeners()
        return view
    }

    private fun initializeViews(view: View) {
        tvStart = view.findViewById(R.id.tvStart)
        tvEnd = view.findViewById(R.id.tvEnd)
        edtTitle = view.findViewById(R.id.edtTitle)
        edtDes = view.findViewById(R.id.edtDes)
        btnCreate = view.findViewById(R.id.btnCreateTask)
    }

    private fun setDefaultDates() {
        tvStart.setText(getFormattedDate(System.currentTimeMillis()))
        tvEnd.setText(getFormattedDate(System.currentTimeMillis()))
    }

    private fun setClickListeners() {
        tvStart.datePicker()
        tvEnd.datePicker()

        btnCreate.setOnClickListener {
            val newDailyTask = DailyTask(
                startDate = parseToDate(tvStart.text.toString()),
                endDate = parseToDate(tvEnd.text.toString()),
                title = edtTitle.text.toString(),
                content = edtDes.text.toString()
            )
            viewModel.addDailyTask(newDailyTask)
            // findNavController().navigate(R.id.action_addTaskFragment_to_dashboardFragment)
        }
    }

    private fun AutoCompleteTextView.datePicker() {
        setOnClickListener {
            val currentDate = Calendar.getInstance()
            var mYear = currentDate.get(Calendar.YEAR)
            var mMonth = currentDate.get(Calendar.MONTH)
            var mDay = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = context?.let { it1 ->
                DatePickerDialog(it1,
                    { _, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        setText(getFormattedDate(calendar.timeInMillis))
                        mDay = dayOfMonth
                        mMonth = month
                        mYear = year
                    }, mYear, mMonth, mDay)
            }
            datePickerDialog?.show()
        }
    }

    private fun getFormattedDate(dateInMillis: Long): String {
        val format = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(dateInMillis)
    }

    private fun parseToDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.parse(dateString) ?: Date()
    }
}
