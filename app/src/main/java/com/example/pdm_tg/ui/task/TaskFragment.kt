package com.example.pdm_tg.ui.task

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pdm_tg.db.Task
import com.example.pdm_tg.db.TaskList
import com.example.pdm_tg.ui.todos.NewTodoFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Date

class TaskFragment : NewTodoFragment() {
    private val viewModel: TaskViewModel by viewModels()
    private val args: TaskFragmentArgs by navArgs()

    init {
        isDetails = true
    }

    override fun fillFields() {
        lifecycleScope.launch {
            val task = viewModel.getTask().await()
            selectedTaskList = task.taskListId?.let { viewModel.getTaskListById(it).await() }
            pickedDate = task.dateDue
            taskNameEditText.setText(task.name)
        }
    }

    override fun onSave(taskName: String, taskList: TaskList?, dueDate: Date): Job {
        return viewModel.updateTask(Task(taskName, taskList?.id, dueDate, args.taskId))
    }
}
