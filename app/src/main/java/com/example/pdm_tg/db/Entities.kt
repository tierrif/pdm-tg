package com.example.pdm_tg.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

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
    val name: String,
    @ColumnInfo(index = true) val taskListId: Long?,
    val dateDue: Date,
    val reminderDate: Date?,
    var isDone: Boolean = false,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)

@Entity
data class Tag(
    val description: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

@Entity
data class TaskList(
    val listName: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
) {
    /**
     * Called by the ArrayAdapter responsible
     * for displaying all task list names in
     * the dropdown list upon creating a new
     * task.
     */
    override fun toString(): String {
        return listName
    }
}

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
    @ColumnInfo(index = true) val taskId: Int,
    @ColumnInfo(index = true) val tagId: Int,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

/**
 * This class is required to convert dates in
 * the database so they are stored properly.
 * It is referenced in the AppDB class in the
 * TypeConverters annotation.
 */
class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long): Date = Date(dateLong)

    @TypeConverter
    fun toTimestamp(date: Date): Long = date.time
}
