package com.example.simbirsofttasks.presentation

import android.app.Application
import com.example.simbirsofttasks.di.DaggerAppComponent

class TaskApp: Application() {
    val component by lazy {
        DaggerAppComponent.factory().create(this)
    }
}