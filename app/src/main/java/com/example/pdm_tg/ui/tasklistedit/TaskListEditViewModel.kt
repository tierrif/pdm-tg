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

    fun getTaskList(): Deferred<TaskList> = viewModelScope.async(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return@async taskListDao.getById(args.taskListId)
    }

    fun updateTaskList(t: TaskList) = viewModelScope.launch(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        taskListDao.update(t)
    }

    fun deleteTaskList(taskListId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        taskListDao.deleteById(taskListId)
    }
}