package com.example.simbirsofttasks.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.simbirsofttasks.data.database.TaskDao
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.domain.repository.TaskRepo
import javax.inject.Inject

class TaskRepoImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val mapper: Mapper
) : TaskRepo {
    override fun getTaskList(): LiveData<List<TaskEntity>> {
        return taskDao.getTaskList().map { taskDBList -> taskDBList.map { mapper.dbToEntity(it) } }
    }

    override suspend fun addTask(task: TaskEntity) {
        taskDao.addTask(mapper.entityToDB(task))
    }

    override suspend fun getTaskById(taskId: Long): TaskEntity{
        return mapper.dbToEntity(taskDao.getTaskById(taskId))
    }

    override suspend fun removeTask(taskId: Long) {
        taskDao.removeTask(taskId)
    }
}