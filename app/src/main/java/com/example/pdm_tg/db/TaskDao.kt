package com.example.pdm_tg.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Query("select * from Task")
    fun get(): List<Task>

    @Query("delete from Task")
    fun deleteAll()

    @Query("select * from Task where taskListId = :taskListId")
    fun getByList(taskListId: Long): List<Task>

    // TODO: this won't work
    @Query("select * from Task where dateDue >= CURRENT_DATE + \" 00:00:00\"")
    fun getToday(): List<Task>

    @Insert
    fun insert(data: Task): Long

    @Update
    fun update(data: Task): Int

    @Delete
    fun delete(data: Task): Int
}