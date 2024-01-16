package com.example.simbirsofttasks.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.domain.useCases.GetTaskByIdUseCase
import com.example.simbirsofttasks.domain.useCases.RemoveTaskUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val removeTaskUseCase: RemoveTaskUseCase
) : ViewModel() {

    private val _task = MutableLiveData<TaskEntity>()
    val task: LiveData<TaskEntity>
        get() = _task

    fun getTaskById(taskId: Long){
        viewModelScope.launch {
            val task = getTaskByIdUseCase(taskId)
            _task.value = task
        }
    }

    fun removeTask(taskId: Long){
        viewModelScope.launch {
            removeTaskUseCase(taskId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}