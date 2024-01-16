package com.example.simbirsofttasks.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.domain.useCases.AddTaskUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    fun addTask(task: TaskEntity){
        viewModelScope.launch {
            addTaskUseCase(task)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}