package com.example.simbirsofttasks.domain.model

import java.time.LocalDateTime

data class TaskEntity(
    val id: Long = 0,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val color: Int,
    val taskName: String,
    val taskDesc: String
) {
    override fun toString(): String {
        return "Id: $id, Task name: $taskName, Start time: $startDateTime, End time: $endDateTime, Color: $color, Task desc: $taskDesc"
    }
}
