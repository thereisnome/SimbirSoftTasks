package com.example.simbirsofttasks.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simbirsofttasks.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (application as TaskApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}