package com.example.pdm_tg.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskTagDao {

    @Query("select * from tasktag")
    fun get() : List<TaskTag>

    @Query("delete from tasktag")
    fun deleteAll()



    @Insert
    fun insert(data: TaskTag): Long

    @Update
    fun update(data: TaskTag): Int

    @Delete
    fun delete(data: TaskTag): Int
}