package com.example.simbirsofttasks.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simbirsofttasks.data.model.TaskDB

@Database(
    entities = [TaskDB::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDataBase : RoomDatabase() {

    companion object {
        private var db: TaskDataBase? = null
        private const val DB_NAME = "Task_DB"
        private val LOCK = Any()

        fun getInstance(context: Context): TaskDataBase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room.databaseBuilder(context, TaskDataBase::class.java, DB_NAME)
                    .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun taskDao(): TaskDao
}