package com.example.pdm_tg.ui.mainlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.TaskListListItemBinding
import com.example.pdm_tg.db.TaskList

private val taskListDiffer = object : DiffUtil.ItemCallback<TaskList>() {
    override fun areItemsTheSame(oldItem: TaskList, newItem: TaskList) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TaskList, newItem: TaskList) = oldItem == newItem
}

class TaskListViewHolder(
    view: View,
    private val onClick: (TaskList) -> Unit,
    private val onLongClick: (TaskList) -> Unit,
) : RecyclerView.ViewHolder(view) {
    private var taskList: TaskList? = null
    private var binding = TaskListListItemBinding.bind(view)

    private val listName = view.findViewById<TextView>(R.id.buttonTextPlanned)

    init {
        binding.root.setOnClickListener {
            onClick(taskList!!)
        }

        binding.root.setOnLongClickListener {
            onLongClick(taskList!!)
            true
        }
    }

    /**
     * Bind a task list to the RecyclerView's
     * ViewHolder, setting its name.
     *
     * @param taskList The task list to bind.
     */
    fun bind(taskList: TaskList) {
        this.taskList = taskList
        listName.text = taskList.listName
    }
}

class TaskListAdapter(
    private val onClick: (TaskList) -> Unit,
    private val onLongClick: (TaskList) -> Unit,
    differ: DiffUtil.ItemCallback<TaskList> = taskListDiffer,
) : ListAdapter<TaskList, TaskListViewHolder>(differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        // Inflate the task list item layout.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_list_list_item, parent, false)

        // Return the view holder with the onClick and onLongClick listeners.
        return TaskListViewHolder(view, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        // Bind the item in this position.
        holder.bind(getItem(position))
    }
}
