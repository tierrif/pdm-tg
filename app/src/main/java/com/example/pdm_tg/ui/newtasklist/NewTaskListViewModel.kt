package com.example.pdm_tg.ui.newtasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.TaskList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewTaskListViewModel(app: Application) : AndroidViewModel(app) {
    /**
     * Create a new task list in the Room
     * database.
     *
     * @param taskName The task name.
     * @return The job that can be joined upon completion.
     */
    fun newTaskList(taskName: String) = viewModelScope.launch(Dispatchers.IO) {
        val taskList = TaskList(taskName)
        val taskListDao = AppDB(getApplication()).taskListDao()

        // Insert.
        taskListDao.insert(taskList)
    }

    /**
     * Get a task list by its name.
     *
     * @param name task list name.
     * @return The awaitable TaskList.
     */
    fun getTaskListByName(name: String): Deferred<TaskList?> = viewModelScope.async(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return@async taskListDao.getByName(name)
    }
}