package com.example.simbirsofttasks.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.domain.useCases.GetTaskListUseCase
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val getTaskListUseCase: GetTaskListUseCase,
) : ViewModel() {

    fun getTaskList(): LiveData<List<TaskEntity>> {
        return getTaskListUseCase()
    }
}