package com.example.pdm_tg.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        TaskList::class, ["idTask"], ["idTaskList"],
        ForeignKey.CASCADE, ForeignKey.CASCADE
    )]
)
data class Task(
    val name: String,
    @PrimaryKey(autoGenerate = true) val idTask: Long = 0
)

@Entity(
    foreignKeys = [ForeignKey(
        Task::class, ["idReminder"], ["idTask"],
        ForeignKey.CASCADE, ForeignKey.CASCADE
    )]
)
data class Reminder(
    @PrimaryKey(autoGenerate = true) val idReminder: Long = 0
)

@Entity
data class Tag(
    val description: String,
    @PrimaryKey(autoGenerate = true) val idTag: Long = 0
)

@Entity
data class TaskList(
    val listName: String,
    @PrimaryKey(autoGenerate = true) val idTaskList: Long = 0
)

@Entity(
    foreignKeys = [ForeignKey(
        Task::class, ["idTask"], ["idTask"],
        ForeignKey.CASCADE, ForeignKey.CASCADE
    ), ForeignKey(
        Tag::class, ["idTag"], ["idTag"],
        ForeignKey.CASCADE, ForeignKey.CASCADE
    )]
)
data class TaskTag(
    val idTask: Int,
    val idTag: Int,
    @PrimaryKey(autoGenerate = true) val idTaskList: Long = 0
)
