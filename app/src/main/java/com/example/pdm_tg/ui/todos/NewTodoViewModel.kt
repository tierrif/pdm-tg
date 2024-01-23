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

class NewTodoViewModel(app: Application) : AndroidViewModel(app) {
    fun newTask(
        taskName: String, taskList: TaskList?, dueDate: Date, reminderDate: Date?, notes: String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        val taskDao = AppDB(getApplication()).taskDao()
        taskDao.insert(Task(taskName, taskList?.id, dueDate, reminderDate, false, notes))
    }

    fun getTaskLists(): Deferred<List<TaskList>> = viewModelScope.async(Dispatchers.IO) {
        val taskListDao = AppDB(getApplication()).taskListDao()
        return@async taskListDao.get()
    }
}
