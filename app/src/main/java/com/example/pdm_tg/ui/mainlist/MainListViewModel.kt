package com.example.pdm_tg.ui.mainlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.TaskList

class MainListViewModel(app: Application) : AndroidViewModel(app) {
    /**
     * Get all tasks lists from the database,
     * in live data.
     *
     * @return Live data with the list of task lists.
     */
    fun getTaskLists(): LiveData<List<TaskList>> {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return taskListDao.getLive()
    }
}
