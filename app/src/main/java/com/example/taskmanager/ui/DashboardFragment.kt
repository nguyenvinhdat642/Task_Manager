package com.example.taskmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskmanager.DailyTask
import com.example.taskmanager.DailyTaskViewModel
import com.example.taskmanager.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.type.Date
import java.text.SimpleDateFormat
import java.util.Locale


class DashboardFragment : Fragment() {
    private val viewModel: DailyTaskViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var username: TextView
    private lateinit var date: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)
        val dailyTaskList = viewModel.getAllDailyTasks()
        val dailyTaskAdapter = DailyTaskAdapter(dailyTaskList)
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
        val currentDate = java.util.Date()
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
}

private class DailyTaskAdapter(
    private val dataset: List<DailyTask>
): RecyclerView.Adapter<DailyTaskAdapter.DailyTaskViewHolder>(){
    inner class DailyTaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvTaskTitle = itemView.findViewById<TextView>(R.id.taskTitle)
        val rbtnIsDone = itemView.findViewById<RadioButton>(R.id.isDone)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item1, parent, false)
        return DailyTaskViewHolder(itemView)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: DailyTaskViewHolder, position: Int) {
        val currentDailyTask = dataset[position]
        holder.tvTaskTitle.text = currentDailyTask.title
        holder.rbtnIsDone.isChecked = currentDailyTask.state == true
    }

}
