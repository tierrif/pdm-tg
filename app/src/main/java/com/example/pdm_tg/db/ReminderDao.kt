package com.example.pdm_tg.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReminderDao {

    @Query("select * from Reminder")
    fun get() : List<Reminder>

    @Query("delete from Reminder")
    fun deleteAll()

    @Insert
    fun insert(data: Reminder): Long

    @Update
    fun update(data: Reminder): Int

    @Delete
    fun delete(data: Reminder): Int
}