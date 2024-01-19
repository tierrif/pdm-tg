package com.example.pdm_tg.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = TaskList::class,
        parentColumns = ["id"],
        childColumns = ["taskListId"],
        onDelete = CASCADE,
        onUpdate = CASCADE,
    )]
)
data class Task(
    val name: String,
    val taskListId: Long,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = CASCADE,
        onUpdate = CASCADE,
    )]
)
data class Reminder(
    val taskId: Long,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

@Entity
data class Tag(
    val description: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

@Entity
data class TaskList(
    val listName: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = CASCADE,
        onUpdate = CASCADE,
    ), ForeignKey(
        entity = Tag::class,
        parentColumns = ["id"],
        childColumns = ["tagId"],
        onDelete = CASCADE,
        onUpdate = CASCADE,
    )]
)
data class TaskTag(
    val taskId: Int,
    val tagId: Int,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)
