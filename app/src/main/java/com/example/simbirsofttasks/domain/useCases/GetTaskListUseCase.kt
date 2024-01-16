package com.example.simbirsofttasks.domain.useCases

import androidx.lifecycle.LiveData
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.domain.repository.TaskRepo
import javax.inject.Inject

class GetTaskListUseCase @Inject constructor(private val taskRepo: TaskRepo){

    operator fun invoke(): LiveData<List<TaskEntity>>{
        return taskRepo.getTaskList()
    }
}