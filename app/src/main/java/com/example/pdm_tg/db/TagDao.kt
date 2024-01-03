package com.example.pdm_tg.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TagDao {

    @Query("select * from Tag")
    fun get() : List<Tag>

    @Query("delete from Tag")
    fun deleteAll()

    @Insert
    fun insert(data: Tag): Long

    @Update
    fun update(data: Tag): Int

    @Delete
    fun delete(data: Tag): Int
}