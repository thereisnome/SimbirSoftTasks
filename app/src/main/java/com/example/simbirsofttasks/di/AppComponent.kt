package com.example.simbirsofttasks.di

import android.app.Application
import com.example.simbirsofttasks.presentation.MainActivity
import com.example.simbirsofttasks.presentation.fragments.AddTaskFragment
import com.example.simbirsofttasks.presentation.fragments.TaskDetailsFragment
import com.example.simbirsofttasks.presentation.fragments.TaskListFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [DataModule::class, ViewModelModule::class]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: TaskListFragment)

    fun inject(fragment: AddTaskFragment)

    fun inject(fragment: TaskDetailsFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}