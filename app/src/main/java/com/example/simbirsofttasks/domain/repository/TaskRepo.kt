package com.example.simbirsofttasks.domain.repository

import androidx.lifecycle.LiveData
import com.example.simbirsofttasks.domain.model.TaskEntity

interface TaskRepo {

    fun getTaskList(): LiveData<List<TaskEntity>>

    suspend fun addTask(task: TaskEntity)

    suspend fun getTaskById(taskId: Long): TaskEntity

    suspend fun removeTask(taskId: Long)

}