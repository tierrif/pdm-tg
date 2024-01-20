package com.example.pdm_tg.ui.task

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pdm_tg.R
import com.example.pdm_tg.db.Task
import com.example.pdm_tg.db.TaskList
import com.example.pdm_tg.ui.todos.NewTodoFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Date

class TaskFragment : NewTodoFragment(), MenuProvider {
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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.task_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.delete) {
            deleteTask()
            return true
        }

        return false
    }

    override fun onStart() {
        super.onStart()
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    private fun deleteTask() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.deleteTaskDialogTitle))
            .setMessage(resources.getString(R.string.deleteTaskDialogDescription))
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    viewModel.deleteTask(args.taskId).join()
                    findNavController().popBackStack()
                }
            }
            .setNeutralButton("Cancel", null)
            .show()
    }
}
