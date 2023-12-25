package com.example.taskmanager.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

    private lateinit var tvStart: TextView
    private lateinit var tvEnd: TextView
    private lateinit var edtTitle: EditText
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
        tvStart.text = getFormattedDate(System.currentTimeMillis())
        tvEnd.text = getFormattedDate(System.currentTimeMillis())
    }

    private fun setClickListeners() {
        tvStart.datePicker()
        tvEnd.datePicker()

        btnCreate.setOnClickListener {
            val newDailyTask = DailyTask(
                startDate = viewModel.parseToDate(tvStart.text.toString()),
                endDate = viewModel.parseToDate(tvEnd.text.toString()),
                title = edtTitle.text.toString(),
                content = edtDes.text.toString(),
                state = false
            )
            viewModel.addDailyTask(newDailyTask)
            showAlertDialog()
        }
    }

    private fun TextView.datePicker() {
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
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogStyle)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.add_task_complete_dialog, null)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)

        builder.setView(view)
        val alertDialog = builder.create()
        tvMessage.text = getString(R.string.new_task_has_been_create_successfully)
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_addTaskFragment_to_calendar)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}
