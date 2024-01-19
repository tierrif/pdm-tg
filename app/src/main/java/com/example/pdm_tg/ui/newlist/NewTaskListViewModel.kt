package com.example.pdm_tg.ui.newlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.TaskList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewTaskListViewModel(app: Application) : AndroidViewModel(app) {
    /**
     * Create a new task list in the Room
     * database.
     *
     * @param taskName The task name.
     */
    fun newTaskList(taskName: String) = viewModelScope.launch(Dispatchers.IO) {
        val taskList = TaskList(taskName)
        val taskListDao = AppDB(getApplication()).taskListDao()

        // Insert.
        taskListDao.insert(taskList)
    }
}