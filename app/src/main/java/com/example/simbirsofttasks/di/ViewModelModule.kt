package com.example.simbirsofttasks.di

import androidx.lifecycle.ViewModel
import com.example.simbirsofttasks.presentation.viewModels.AddTaskViewModel
import com.example.simbirsofttasks.presentation.viewModels.TaskDetailsViewModel
import com.example.simbirsofttasks.presentation.viewModels.TaskListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = TaskListViewModel::class)
    fun bindTaskListViewModel(viewModel: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AddTaskViewModel::class)
    fun bindAddTaskViewModel(viewModel: AddTaskViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = TaskDetailsViewModel::class)
    fun bindTaskDetailsViewModel(viewModel: TaskDetailsViewModel): ViewModel
}