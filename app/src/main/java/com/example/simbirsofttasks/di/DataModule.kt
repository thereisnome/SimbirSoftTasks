package com.example.simbirsofttasks.di

import android.app.Application
import com.example.simbirsofttasks.data.TaskRepoImpl
import com.example.simbirsofttasks.data.database.TaskDao
import com.example.simbirsofttasks.data.database.TaskDataBase
import com.example.simbirsofttasks.domain.repository.TaskRepo
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @AppScope
    fun bindTaskRepo(impl: TaskRepoImpl): TaskRepo

    companion object{

        @Provides
        @AppScope
        fun provideCoinDao(
            application: Application
        ): TaskDao{
            return TaskDataBase.getInstance(application).taskDao()
        }
    }
}