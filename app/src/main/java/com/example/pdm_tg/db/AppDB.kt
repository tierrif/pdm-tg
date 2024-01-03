package com.example.pdm_tg.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class AppDB : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: AppDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context.applicationContext).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDB::class.java, "todoapp.db")
                .allowMainThreadQueries() // for now :)
                .fallbackToDestructiveMigration()
                .build()
    }
}