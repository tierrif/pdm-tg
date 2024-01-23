package com.example.pdm_tg.ui.tasklistlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.TaskList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class TaskListListViewModel(app: Application) : AndroidViewModel(app) {
    fun getTaskLists(): LiveData<List<TaskList>> {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return taskListDao.getLive()
    }
}
