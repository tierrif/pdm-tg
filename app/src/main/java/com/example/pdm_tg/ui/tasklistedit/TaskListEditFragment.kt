package com.example.pdm_tg.ui.tasklistedit

import android.util.Log
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
import com.example.pdm_tg.db.TaskList
import com.example.pdm_tg.ui.newtasklist.NewTaskListFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TaskListEditFragment : NewTaskListFragment(), MenuProvider {
    private val viewModel: TaskListEditViewModel by viewModels()
    private val args: TaskListEditFragmentArgs by navArgs()

    init {
        isDetails = true
    }

    override fun fillFields() {
        lifecycleScope.launch {
            val taskList = viewModel.getTaskList().await()
            name.setText(taskList.listName)
        }
    }

    override fun onSave(t: TaskList): Job {
        t.id = args.taskListId
        return viewModel.updateTaskList(t)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        if (menu.findItem(R.id.delete) === null) {
            menuInflater.inflate(R.menu.task_menu, menu)
        }
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
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.edit_list)
    }

    private fun deleteTask() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.deleteTaskListDialogTitle))
            .setMessage(resources.getString(R.string.deleteTaskDialogDescription))
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    viewModel.deleteTaskList(args.taskListId).join()
                    findNavController().popBackStack()
                }
            }
            .setNeutralButton("Cancel", null)
            .show()
    }
}
