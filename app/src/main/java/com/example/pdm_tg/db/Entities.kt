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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val taskListId: Long,
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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: Long,
)

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
)

@Entity
data class TaskList(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listName: String,
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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: Int,
    val tagId: Int,
)
