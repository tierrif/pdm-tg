package com.example.pdm_tg.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Query("select * from task")
    abstract fun get() : List<Task>

    @Query("delete from task")
    abstract fun deleteAll()

    @Insert
    fun insert(data: Task): Long

    @Update
    fun update(data: Task): Int

    @Delete
    fun delete(data: Task): Int
}