package com.example.taskmanager.ui

import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.DailyTask
import com.example.taskmanager.DailyTaskViewModel
import com.example.taskmanager.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private lateinit var selectedYearMoth: YearMonth
    private lateinit var selectedDate: LocalDate
    private lateinit var data: List<LocalDate>
//    private lateinit var dailyTaskData: List<DailyTask>
    private lateinit var domAdapter: DomAdapter
    private lateinit var dailyTaskAdapter: DomDailyTaskAdapter
    private val viewModel: DailyTaskViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val calendarMonth = view.findViewById<TextView>(R.id.calendarMonth)
        val domList = view.findViewById<RecyclerView>(R.id.dayOfMonthList)
        val dailyTaskList = view.findViewById<RecyclerView>(R.id.dailyTaskList)
        val btnAddTask = view.findViewById<Button>(R.id.btnAddTask)

        calendarMonth.monthPicker()
        data = generateDaysInMonth(selectedYearMoth)
        domAdapter = DomAdapter(data)
        domList.adapter = domAdapter
        domList.setHasFixedSize(true)

//        dailyTaskAdapter = DomDailyTaskAdapter(dailyTaskData)
//        dailyTaskList.adapter = dailyTaskAdapter

        btnAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_calendar_to_addTaskFragment)
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun TextView.monthPicker() {
        // Set initial text
        setInitialDateText()
        this.setOnClickListener {
            val calendar = Calendar.getInstance()
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month, dayOfMonth)
                    selectedYearMoth = YearMonth.of(year, month + 1)
                    this.text = formatSelectedDate(year, month)

                    data = generateDaysInMonth(selectedYearMoth)
                    domAdapter.updateData(data)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun TextView.setInitialDateText() {
        // Get current date
        val currentDate = LocalDate.now()
        selectedYearMoth = YearMonth.from(currentDate)
        // Format current date
        val formatter = DateTimeFormatter.ofPattern("MMM, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)
        this.text = formattedDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatSelectedDate(year: Int, month: Int): String {
        val formatMonth = Month.of(month + 1)
            .name
            .substring(0, 3)
            .lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        return "${formatMonth}, $year"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateDaysInMonth(yearMonth: YearMonth): List<LocalDate> {
        return (1..yearMonth.lengthOfMonth())
            .map { LocalDate.of(yearMonth.year, yearMonth.month, it) }
    }
}

private class DomDailyTaskAdapter(private val dailyTaskData: List<DailyTask>): RecyclerView.Adapter<DomDailyTaskAdapter.DomDailyTaskViewHolder>() {
    inner class DomDailyTaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvTaskTitle = itemView.findViewById<TextView>(R.id.taskTitle)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomDailyTaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item1, parent, false)
        return DomDailyTaskViewHolder(itemView)
    }

    override fun getItemCount() = dailyTaskData.size

    override fun onBindViewHolder(holder: DomDailyTaskViewHolder, position: Int) {
        val currentDailyTask = dailyTaskData[position]
        holder.tvTaskTitle.text = currentDailyTask.title
    }
}

private class DomAdapter(private var data: List<LocalDate>) :
    RecyclerView.Adapter<DomAdapter.DomViewHolder>() {
    inner class DomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeek = itemView.findViewById<TextView>(R.id.tvDayOfWeek)
        val day = itemView.findViewById<TextView>(R.id.tvDayOfMonth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_of_month_list_item, parent, false)
        return DomViewHolder(itemView)
    }

    override fun getItemCount() = data.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DomViewHolder, position: Int) {
        val currentDom = data[position]
        holder.day.text = currentDom.dayOfMonth.toString()
        holder.dayOfWeek.text = currentDom.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    }

    fun updateData(newData: List<LocalDate>) {
        data = newData
        notifyDataSetChanged()
    }
}
