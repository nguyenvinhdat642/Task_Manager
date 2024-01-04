package com.example.taskmanager.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.model.DailyTask
import com.example.taskmanager.viewModel.DailyTaskViewModel
import com.example.taskmanager.R
import com.example.taskmanager.model.Plan
import com.example.taskmanager.viewModel.PlanViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class DashboardFragment : Fragment() {
    private val viewModel: DailyTaskViewModel by activityViewModels()
    private val planViewModel: PlanViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var username: TextView
    private lateinit var date: TextView
    private lateinit var dailyTaskAdapter: DailyTaskAdapter
    private lateinit var planAdapter: PlanAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)
        planAdapter = PlanAdapter(
            dataset = planViewModel.getAllPlans()
        )
        dailyTaskAdapter = DailyTaskAdapter(
            dataset = viewModel.getAllDailyTasks(),
            onItemClick = { selectedTask ->
                viewModel.selectedDailyTask(selectedTask)
                findNavController().navigate(R.id.action_home_to_dailyTaskFragment)
            },
            onItemLongClick = { selectedTask ->
                showAlertDialog(selectedTask)
            })
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        username = view.findViewById(R.id.tvHello)
        date = view.findViewById(R.id.tvCurrentDate)

        auth = FirebaseAuth.getInstance()

        setDate()

        loadUserProfile()

        recyclerView.adapter = dailyTaskAdapter
        recyclerView.setHasFixedSize(true)
        return view
    }

    private fun setDate() {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEEE, dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        date.text = formattedDate
    }

    private fun loadUserProfile() {
        currentUser = auth.currentUser
        currentUser?.let { user ->
            username.text = user.displayName
        }
    }

    override fun onResume() {
        super.onResume()
        dailyTaskAdapter.updateDataset(viewModel.getAllDailyTasks())
    }

    private fun showAlertDialog(dailyTask: DailyTask) {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogStyle)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.long_click_dialog, null)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        builder.setView(view)
        val alertDialog = builder.create()
        btnDelete.setOnClickListener {
            showDeleteAlertDialog(dailyTask)
            alertDialog.dismiss()
        }
        btnEdit.setOnClickListener {
            viewModel.selectedDailyTask(dailyTask)
            findNavController().navigate(R.id.action_home_to_editFragment)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showDeleteAlertDialog(dailyTask: DailyTask) {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogStyle)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.delete_dialog, null)
        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)

        builder.setView(view)
        val alertDialog = builder.create()
        btnYes.setOnClickListener {
            viewModel.deleteTask(dailyTask)
            alertDialog.dismiss()
            dailyTaskAdapter.updateDataset(viewModel.getAllDailyTasks()) // Move notifyDataSetChanged here
        }
        btnNo.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

}

private class PlanAdapter(private var dataset: List<Plan>) :
    RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {
    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeRemaining = itemView.findViewById<TextView>(R.id.time_remaining)
        val planTitle = itemView.findViewById<TextView>(R.id.plan_title)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_item, parent, false)
        return PlanViewHolder(view)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val currentPlan = dataset[position]
        holder.planTitle.text = currentPlan.title
        holder.timeRemaining.text =
            calculateRemainingDays(currentPlan.startDate, currentPlan.endDate).toString()
        holder.progressBar.progress = 50
    }

    fun calculateRemainingDays(startDate: Date, endDate: Date): Long {
        // Convert Date objects to Calendar instances
        val startCalendar = Calendar.getInstance().apply { time = startDate }
        val endCalendar = Calendar.getInstance().apply { time = endDate }

        // Set the time of the endCalendar to the end of the day
        endCalendar.set(Calendar.HOUR_OF_DAY, 23)
        endCalendar.set(Calendar.MINUTE, 59)
        endCalendar.set(Calendar.SECOND, 59)
        endCalendar.set(Calendar.MILLISECOND, 999)

        // Calculate the difference in milliseconds
        val timeDifference = endCalendar.timeInMillis - startCalendar.timeInMillis

        // Convert milliseconds to days

        return timeDifference / (1000 * 60 * 60 * 24)
    }

}

private class DailyTaskAdapter(
    private var dataset: List<DailyTask>,
    private val onItemClick: (DailyTask) -> Unit,
    private val onItemLongClick: (DailyTask) -> Unit
) : RecyclerView.Adapter<DailyTaskAdapter.DailyTaskViewHolder>() {
    inner class DailyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTaskTitle = itemView.findViewById<TextView>(R.id.taskTitle)
        val rbtnIsDone = itemView.findViewById<RadioButton>(R.id.isDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTaskViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.task_item1, parent, false)
        return DailyTaskViewHolder(itemView)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: DailyTaskViewHolder, position: Int) {
        val currentDailyTask = dataset[position]
        holder.tvTaskTitle.textSize = 24F
        holder.tvTaskTitle.setTypeface(null, Typeface.BOLD)
        holder.tvTaskTitle.text = currentDailyTask.title
        holder.rbtnIsDone.isChecked = currentDailyTask.state == true
        if(currentDailyTask.state){
            holder.tvTaskTitle.setTextColor(Color.parseColor("#006EE9"))
        }
        holder.itemView.setOnClickListener {
            onItemClick.invoke(currentDailyTask)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick.invoke(currentDailyTask)
            true
        }
    }

    fun updateDataset(newDataset: List<DailyTask>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(
                oldList = dataset,
                newList = newDataset,
                areItemsTheSame = { oldItem, newItem -> oldItem.taskId == newItem.taskId },
                areContentsTheSame = { oldItem, newItem -> oldItem == newItem })
        )
        dataset = newDataset
        diffResult.dispatchUpdatesTo(this)
    }
}

class DiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    private val areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }
}
