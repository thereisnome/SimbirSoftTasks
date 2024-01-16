package com.example.simbirsofttasks.domain.useCases

import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.domain.repository.TaskRepo
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepo: TaskRepo) {

    suspend operator fun invoke(task: TaskEntity){
        taskRepo.addTask(task)
    }
}