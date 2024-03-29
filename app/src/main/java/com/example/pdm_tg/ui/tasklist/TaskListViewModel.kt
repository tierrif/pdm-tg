package com.example.pdm_tg.ui.tasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.Task
import com.example.pdm_tg.ui.mainlist.MainListFragment.ListType.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {
    private val args = TaskListFragmentArgs.fromSavedStateHandle(savedStateHandle)

    /**
     * Get the list of tasks according to the
     * args passed to the fragment in live data.
     *
     * @return The list of tasks in live data.
     */
    fun getTaskList(): LiveData<List<Task>> {
        val db = AppDB(getApplication())

        return when (args.type) {
            CUSTOM -> db.taskDao().getByList(args.listId)
            MY_DAY -> db.taskDao().getToday()
            PLANNED -> db.taskDao().get()
        }
    }

    /**
     * Update a task.
     *
     * @param task The task to update.
     * @return The job that can be joined upon completion.
     */
    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        val taskDao = AppDB(getApplication()).taskDao()
        taskDao.update(task)
    }
}
