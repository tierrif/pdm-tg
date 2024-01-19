package com.example.pdm_tg.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Query("select * from Task")
    fun get(): LiveData<List<Task>>

    @Query("delete from Task")
    fun deleteAll()

    @Query("select * from Task where taskListId = :taskListId")
    fun getByList(taskListId: Long): LiveData<List<Task>>

    // https://stackoverflow.com/questions/14709661/sqlite-query-for-dates-equals-today
    @Query("select * from Task where date(datetime(dateDue / 1000 , 'unixepoch')) = date('now')")
    fun getToday(): LiveData<List<Task>>

    @Insert
    fun insert(data: Task): Long

    @Update
    fun update(data: Task): Int

    @Delete
    fun delete(data: Task): Int
}