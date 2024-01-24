package com.example.pdm_tg.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Task::class, TaskList::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDB : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun taskListDao(): TaskListDao

    companion object {
        @Volatile
        private var instance: AppDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context.applicationContext).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDB::class.java, "todoapp.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
