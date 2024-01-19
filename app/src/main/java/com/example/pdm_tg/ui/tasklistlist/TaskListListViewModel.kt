package com.example.pdm_tg.ui.tasklistlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.TaskList

class TaskListListViewModel(app: Application) : AndroidViewModel(app) {
    fun getTaskLists(): LiveData<List<TaskList>> {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return taskListDao.getLive()
    }
}