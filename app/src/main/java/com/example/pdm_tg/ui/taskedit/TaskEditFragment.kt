package com.example.pdm_tg.ui.taskedit

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pdm_tg.R
import com.example.pdm_tg.db.Task
import com.example.pdm_tg.ui.newtask.NewTaskFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TaskEditFragment : NewTaskFragment(), MenuProvider {
    private val viewModel: TaskEditViewModel by viewModels()
    private val args: TaskEditFragmentArgs by navArgs()

    init {
        isDetails = true
    }

    override fun fillFields() {
        lifecycleScope.launch {
            val task = viewModel.getTask().await()
            selectedTaskList = task.taskListId?.let { viewModel.getTaskListById(it).await() }
            pickedDate = task.dateDue
            pickedReminder = task.reminderDate
            taskNameEditText.setText(task.name)
            taskNotesEditText.setText(task.notes)
        }
    }

    override fun onSave(t: Task): Job {
        t.id = args.taskId
        return viewModel.updateTask(t)
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        if (menu.findItem(R.id.delete) === null) {
            menuInflater.inflate(R.menu.task_menu, menu)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.delete && context !== null) {
            deleteTask()
            return true
        }

        return false
    }

    override fun onStart() {
        super.onStart()
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.edit_task)
    }

    /**
     * Show a confirmation dialog to delete
     * a task and delete it from the database
     * if the user positively responds.
     */
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
