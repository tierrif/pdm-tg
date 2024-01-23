package com.example.pdm_tg.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskListDao {

    @Query("select * from TaskList")
    fun getLive() : LiveData<List<TaskList>>

    @Query("select * from TaskList")
    fun get(): List<TaskList>

    @Query("select * from TaskList where id = :id")
    fun getById(id: Long): TaskList

    @Query("select * from TaskList where listName LIKE :name")
    fun getByName(name: String): TaskList?

    @Query("delete from TaskList")
    fun deleteAll()

    @Insert
    fun insert(data: TaskList): Long

    @Update
    fun update(data: TaskList): Int

    @Delete
    fun delete(data: TaskList): Int
}