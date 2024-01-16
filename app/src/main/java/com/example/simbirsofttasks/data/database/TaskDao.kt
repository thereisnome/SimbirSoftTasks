package com.example.simbirsofttasks.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simbirsofttasks.data.model.TaskDB

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_list")
    fun getTaskList(): LiveData<List<TaskDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskDB)

    @Query("SELECT * FROM task_list WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): TaskDB

    @Query("DELETE FROM task_list WHERE id = :taskId")
    suspend fun removeTask(taskId: Long)
}