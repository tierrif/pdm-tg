package com.example.pdm_tg.ui.tasklistedit

import com.example.pdm_tg.db.TaskList
import com.example.pdm_tg.ui.newlist.NewListFragment
import kotlinx.coroutines.Job

class TaskListEditFragment : NewListFragment() {

    override fun fillFields() {

    }

    override fun onSave(t: TaskList): Job {
        return Job()
    }
}