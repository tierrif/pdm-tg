package com.example.pdm_tg.ui.tasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.pdm_tg.db.AppDB
import com.example.pdm_tg.db.Task
import com.example.pdm_tg.ui.tasklistlist.TaskListListFragment.ListType.*

class TaskListViewModel(
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {
    private val args = TaskListFragmentArgs.fromSavedStateHandle(savedStateHandle)

    /**
     * Get the list of tasks according to the
     * args passed to the fragment.
     *
     * @return The list of tasks.
     */
    fun getTaskList(): LiveData<List<Task>> {
        val db = AppDB(getApplication())

        // TODO: Fix MY_DAY and CUSTOM
        return when (args.type) {
            CUSTOM -> db.taskDao().getByList(args.listId)
            MY_DAY -> db.taskDao().getToday()
            PLANNED -> db.taskDao().get()
        }
    }
}