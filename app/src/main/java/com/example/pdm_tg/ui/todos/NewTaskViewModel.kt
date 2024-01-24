package com.example.pdm_tg.ui.todos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.Task
import com.example.pdm_tg.db.TaskList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date

class NewTaskViewModel(app: Application) : AndroidViewModel(app) {

    /**
     * Create a new task for the database.
     *
     * @param taskName The task name.
     * @param taskList The task list this task is in (can be null if none).
     * @param dueDate The date this task is due.
     * @param reminderDate The date for a set reminder (can be null if none).
     * @param notes Notes for this task (can be null if none).
     */
    fun newTask(
        taskName: String, taskList: TaskList?, dueDate: Date, reminderDate: Date?, notes: String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        val taskDao = AppDB(getApplication()).taskDao()
        taskDao.insert(Task(taskName, taskList?.id, dueDate, reminderDate, false, notes))
    }

    /**
     * Get all task lists from the database.
     *
     * @return The awaitable list of task lists.
     */
    fun getTaskLists(): Deferred<List<TaskList>> = viewModelScope.async(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return@async taskListDao.get()
    }

    /**
     * Get a task list by its ID.
     *
     * @param taskListId The ID of the task list to get.
     * @return An awaitable task list.
     */
    fun getTaskListById(
        taskListId: Long
    ): Deferred<TaskList?> = viewModelScope.async(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return@async taskListDao.getById(taskListId)
    }
}
