package com.example.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.taskmanager.viewModel.DailyTaskViewModel
import com.example.taskmanager.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class DailyTaskFragment : Fragment() {
    private val viewModel: DailyTaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_task, container, false)
        val tvDailyTaskName = view.findViewById<TextView>(R.id.tvDailyTaskName)
        val tvStart = view.findViewById<TextView>(R.id.tvStart2)
        val tvEnd = view.findViewById<TextView>(R.id.tvEnd2)
        val tvDes = view.findViewById<TextView>(R.id.tvDes)
        val tvHours = view.findViewById<TextView>(R.id.tvHour)
        val tvMinute= view.findViewById<TextView>(R.id.tvMinute)
        val btnClose = view.findViewById<MaterialButton>(R.id.btnClose)
        val btnFinish = view.findViewById<MaterialButton>(R.id.btnFinish)
        val dailyTask = viewModel.selectedDailyTask.value
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val (remainingHours, remainingMinutes) = calculateTimeRemaining(dailyTask?.startDate, dailyTask?.endDate)
        tvStart.text = dailyTask?.startDate?.let { simpleDateFormat.format(it) }
        tvEnd.text = dailyTask?.endDate?.let { simpleDateFormat.format(it) }
        tvDes.text = dailyTask?.content
        tvHours.text = remainingHours.toString()
        tvMinute.text = remainingMinutes.toString()
        tvDailyTaskName.text = dailyTask?.title ?: "NoName"
        if (dailyTask != null) {
            if (dailyTask.state){
                btnFinish.text = "Unfinish"
            }else
                btnFinish.text = "Finish"
        }
        btnClose.setOnClickListener{
            findNavController().navigate(R.id.action_dailyTaskFragment_to_home)
        }
        btnFinish.setOnClickListener {
            if (dailyTask?.state == true){
                dailyTask.taskId.let { it1 -> viewModel.unfinishTask(it1) }
            }else{
                dailyTask?.taskId?.let { it1 -> viewModel.finishTask(it1) }
            }
            findNavController().navigate(R.id.action_dailyTaskFragment_to_home)

        }
        return view
    }

    private fun calculateTimeRemaining(startDate: Date?, endDate: Date?): Pair<Long, Long> {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return Pair(0, 0)
        }
        val currentTimeMillis = System.currentTimeMillis()
        val startMillis = startDate.time
        val endMillis = endDate.time

        if (startDate == endDate) {
            val endOfDayMillis = Calendar.getInstance().apply {
                timeInMillis = currentTimeMillis
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

            return Pair(maxOf(0, endOfDayMillis - currentTimeMillis) / (1000 * 60 * 60), maxOf(0, endOfDayMillis - currentTimeMillis) % (1000 * 60 * 60) / (1000 * 60))
        }

        return Pair(maxOf(0, endMillis - startMillis) / (1000 * 60 * 60), maxOf(0, endMillis - startMillis) % (1000 * 60 * 60) / (1000 * 60))
    }
}
