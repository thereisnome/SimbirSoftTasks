package com.example.simbirsofttasks.domain.useCases

import com.example.simbirsofttasks.domain.repository.TaskRepo
import javax.inject.Inject

class RemoveTaskUseCase  @Inject constructor(private val taskRepo: TaskRepo) {

    suspend operator fun invoke(taskId: Long){
        taskRepo.removeTask(taskId)
    }
}