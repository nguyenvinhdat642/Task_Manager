package com.example.taskmanager.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.taskmanager.viewModel.DailyTaskViewModel
import com.example.taskmanager.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditFragment : Fragment() {

    private val viewModel: DailyTaskViewModel by activityViewModels()

    private lateinit var tvStart: TextView
    private lateinit var tvEnd: TextView
    private lateinit var edtTitle: EditText
    private lateinit var edtDes: EditText
    private lateinit var btnCreate: MaterialButton
    private lateinit var tvEditTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        initializeViews(view)
        setDefaultTask()
        setClickListeners()
        return view
    }

    private fun initializeViews(view: View) {
        tvStart = view.findViewById(R.id.tvStart)
        tvEnd = view.findViewById(R.id.tvEnd)
        edtTitle = view.findViewById(R.id.edtTitle)
        edtDes = view.findViewById(R.id.edtDes)
        btnCreate = view.findViewById(R.id.btnCreateTask)
        tvEditTitle = view.findViewById(R.id.tvEditTitle)
    }

    private fun setDefaultTask() {
        val dailyTask = viewModel.selectedDailyTask.value
        tvStart.text = dailyTask?.startDate?.let { viewModel.parseToString(it) }
        tvEnd.text = dailyTask?.endDate?.let { viewModel.parseToString(it) }
        tvEditTitle.text = dailyTask?.title
        edtTitle.setText(dailyTask?.title.toString())
        edtDes.setText(dailyTask?.content.toString())
    }

    private fun setClickListeners() {
        tvStart.datePicker()
        tvEnd.datePicker()

        btnCreate.setOnClickListener {
            val dailyTask = viewModel.selectedDailyTask.value
            val startDate = viewModel.parseToDate(tvStart.text.toString())
            val endDate = viewModel.parseToDate(tvEnd.text.toString())
            val title = edtTitle.text.toString()
            val content = edtDes.text.toString()
            viewModel.updateTaskById(dailyTask!!.taskId, startDate, endDate, title, content)
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
                        text = getFormattedDate(calendar.timeInMillis)
                        mDay = dayOfMonth
                        mMonth = month
                        mYear = year
                    }, mYear, mMonth, mDay
                )
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
        tvMessage.text = getString(R.string.task_updated_successfully)
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_home)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}