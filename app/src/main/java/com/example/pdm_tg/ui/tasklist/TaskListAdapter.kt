package com.example.pdm_tg.ui.tasklist

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.TaskItemBinding
import com.example.pdm_tg.db.Task
import com.google.android.material.checkbox.MaterialCheckBox
import java.util.Calendar

private val taskDiffer = object : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
}

class TaskViewHolder(
    private val view: View,
    private val onClick: (Task) -> Unit,
    private val onLongClick: (Task) -> Unit,
) : RecyclerView.ViewHolder(view) {
    private var task: Task? = null
    private var binding = TaskItemBinding.bind(view)

    private val checkbox = view.findViewById<MaterialCheckBox>(R.id.checkbox)
    private val taskName = view.findViewById<TextView>(R.id.taskName)
    private val taskDueDate = view.findViewById<TextView>(R.id.taskDueDate)
    private val calendarIcon = view.findViewById<ImageView>(R.id.calendarIcon)

    init {
        binding.root.setOnClickListener {
            onClick(task!!)
            checkbox.checkedState = MaterialCheckBox.STATE_CHECKED
        }

        checkbox.addOnCheckedStateChangedListener { _, state ->
            if (state == MaterialCheckBox.STATE_CHECKED) {
                onClick(task!!)
            }
        }

        binding.root.setOnLongClickListener {
            onLongClick(task!!)
            true
        }
    }

    /**
     * Bind a task to the RecyclerView's
     * ViewHolder, setting its name and due
     * date.
     *
     * @param task The task to bind.
     */
    fun bind(task: Task) {
        this.task = task
        taskName.text = task.name
        taskDueDate.text = DateUtils.getRelativeDateTimeString(
            view.context,
            task.dateDue.time,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE,
        )

        // If it's overdue, set the text to red.
        if (task.dateDue.time < Calendar.getInstance().time.time) {
            taskDueDate.setTextColor(view.context.resources.getColor(R.color.red, null))
            calendarIcon.setImageResource(R.drawable.calendar_red)
        }
    }
}

class TaskAdapter(
    private val onClick: (Task) -> Unit,
    private val onLongClick: (Task) -> Unit,
    differ: DiffUtil.ItemCallback<Task> = taskDiffer,
) : ListAdapter<Task, TaskViewHolder>(differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflate the task item layout.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)

        // Return the view holder with the onClick and onLongClick listeners.
        return TaskViewHolder(view, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Bind the item in this position.
        holder.bind(getItem(position))
    }
}


