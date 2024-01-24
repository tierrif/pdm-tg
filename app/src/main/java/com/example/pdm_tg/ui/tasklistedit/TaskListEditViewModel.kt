package com.example.pdm_tg.ui.tasklistedit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.TaskList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TaskListEditViewModel(
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {
    private val args = TaskListEditFragmentArgs.fromSavedStateHandle(savedStateHandle)

    /**
     * Get the task list in this context.
     *
     * @return An awaitable task list.
     */
    fun getTaskList(): Deferred<TaskList> = viewModelScope.async(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return@async taskListDao.getById(args.taskListId)
    }

    /**
     * Update a task list.
     *
     * @param t The task list to update.
     * @return The job that can be joined upon completion.
     */
    fun updateTaskList(t: TaskList) = viewModelScope.launch(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        taskListDao.update(t)
    }

    /**
     * Delete a task list by ID.
     *
     * @param taskListId The task list's ID.
     * @return A job that can be joined upon completion.
     */
    fun deleteTaskList(taskListId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        taskListDao.deleteById(taskListId)
    }
}