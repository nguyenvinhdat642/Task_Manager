package com.example.taskmanager.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.model.DailyTask
import com.example.taskmanager.viewModel.DailyTaskViewModel
import com.example.taskmanager.R
import com.example.taskmanager.model.Todo
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskFragment : Fragment() {

    private val viewModel: DailyTaskViewModel by activityViewModels()

    private lateinit var tvStart: TextView
    private lateinit var tvEnd: TextView
    private lateinit var edtTitle: EditText
    private lateinit var edtDes: EditText
    private lateinit var btnCreate: MaterialButton
    private lateinit var btnNavigateBack: Button
    private lateinit var addTodo: TextView
    private lateinit var todoList: ArrayList<Todo>
    private lateinit var todoListAdapter: TodoListAdapter

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
        val todolistRecyclerView = view.findViewById<RecyclerView>(R.id.todolist_recycler_view)
        tvStart = view.findViewById(R.id.tvStart)
        tvEnd = view.findViewById(R.id.tvEnd)
        edtTitle = view.findViewById(R.id.edtTitle)
        edtDes = view.findViewById(R.id.edtDes)
        btnCreate = view.findViewById(R.id.btnCreateTask)
        btnNavigateBack = view.findViewById(R.id.navigate2)
        todoList = arrayListOf()
        addTodo = view.findViewById(R.id.add_todo)
        todoListAdapter = TodoListAdapter(todoList)
        todolistRecyclerView.adapter = todoListAdapter
        todolistRecyclerView.setHasFixedSize(true)
        todolistRecyclerView.isNestedScrollingEnabled = false


    }

    private fun setDefaultDates() {
        tvStart.text = getFormattedDate(System.currentTimeMillis())
        tvEnd.text = getFormattedDate(System.currentTimeMillis())
    }

    private fun setClickListeners() {
        tvStart.datePicker()
        tvEnd.datePicker()
        addTodo.setOnClickListener {
            showAddTodoDialog()
        }
        btnCreate.setOnClickListener {
            val start = viewModel.parseToDate(tvStart.text.toString())
            val end = viewModel.parseToDate(tvEnd.text.toString())
            if (edtTitle.text.isEmpty()) {
                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_LONG)
                    .show()
            } else {
                if (start <= end) {
                    val newDailyTask = DailyTask(
                        startDate = start,
                        endDate = end,
                        title = edtTitle.text.toString(),
                        content = edtDes.text.toString(),
                        state = false
                    )
                    viewModel.addDailyTask(newDailyTask)
                    showAlertDialog()
                } else {
                    Toast.makeText(
                        context,
                        "End date cannot be before start date",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
        btnNavigateBack.setOnClickListener {
            findNavController().navigate(R.id.action_addTaskFragment_to_calendar)
        }
    }

    private fun TextView.datePicker() {
        setOnClickListener {
            val currentDate = Calendar.getInstance()
            var mYear = currentDate.get(Calendar.YEAR)
            var mMonth = currentDate.get(Calendar.MONTH)
            var mDay = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = context?.let { it1 ->
                DatePickerDialog(
                    it1,
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
        tvMessage.text = getString(R.string.new_task_has_been_create_successfully)
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_addTaskFragment_to_calendar)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showAddTodoDialog() {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogStyle)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.add_todo_dialog, null)
        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        val todoTitle = view.findViewById<TextView>(R.id.todo_title)

        builder.setView(view)
        val alertDialog = builder.create()
        btnYes.setOnClickListener {
            val todo = Todo(
                title = todoTitle.text.toString(),
                state = false
            )
            todoList.add(todo)
            todoListAdapter.notifyDataSetChanged()
            alertDialog.dismiss()
        }
        btnNo.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}

private class TodoListAdapter(
    private var dataset: List<Todo>
) : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {
    inner class TodoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTaskTitle = itemView.findViewById<TextView>(R.id.taskTitle)
        val rbtnIsDone = itemView.findViewById<RadioButton>(R.id.isDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.task_item1, parent, false)
        return TodoListViewHolder(itemView)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: TodoListAdapter.TodoListViewHolder, position: Int) {
        val currentTodo = dataset[position]
        holder.tvTaskTitle.text = currentTodo.title
        holder.rbtnIsDone.isChecked = currentTodo.state == true
        holder.rbtnIsDone.visibility = View.GONE
    }

}

