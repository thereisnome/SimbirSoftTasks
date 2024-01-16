package com.example.simbirsofttasks.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list")
data class TaskDB(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val startDateTime: String,
    val endDateTime: String,
    val color: Int,
    val taskName: String,
    val taskDesc: String
)
