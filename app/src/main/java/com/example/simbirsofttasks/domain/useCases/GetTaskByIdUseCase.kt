package com.example.simbirsofttasks.domain.useCases

import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.domain.repository.TaskRepo
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(private val taskRepo: TaskRepo) {

    suspend operator fun invoke(taskId: Long): TaskEntity{
        return taskRepo.getTaskById(taskId)
    }
}