package com.example.pdm_tg.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.Task
import com.example.pdm_tg.db.TaskList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TaskViewModel(app: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(app) {
    private val args = TaskFragmentArgs.fromSavedStateHandle(savedStateHandle)

    /**
     * Get a task by the ID passed in args.
     * Await this coroutine in the lifecycle scope.
     *
     * @return The awaitable task.
     */
    fun getTask(): Deferred<Task> = viewModelScope.async(Dispatchers.IO) {
        val taskDao = AppDB(getApplication()).taskDao()
        return@async taskDao.getById(args.taskId)
    }

    /**
     * Get a task list by the ID passed in the argument.
     *
     * @param id The ID of the task list to retrieve
     * @return The task list.
     */
    fun getTaskListById(id: Long): Deferred<TaskList> = viewModelScope.async(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return@async taskListDao.getById(id)
    }

    /**
     * Update a specific task.
     *
     * @param task The task to update with the
     *             updated data.
     * @return The job that can be joined.
     */
    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        val taskDao = AppDB(getApplication()).taskDao()
        taskDao.update(task)
    }
}
