package com.example.simbirsofttasks.data

import com.example.simbirsofttasks.data.model.TaskDB
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.presentation.customView.TaskViewTaskModel
import java.time.LocalDateTime
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun entityToDB(task: TaskEntity): TaskDB = TaskDB(
        id = task.id,
        startDateTime = task.startDateTime.toString(),
        endDateTime = task.endDateTime.toString(),
        color = task.color,
        taskName = task.taskName,
        taskDesc = task.taskDesc
    )

    fun dbToEntity(task: TaskDB): TaskEntity = TaskEntity(
        id = task.id,
        startDateTime = LocalDateTime.parse(task.startDateTime),
        endDateTime = LocalDateTime.parse(task.endDateTime),
        color = task.color,
        taskName = task.taskName,
        taskDesc = task.taskDesc
    )

    fun entityToTaskViewTaskModel(task: TaskEntity): TaskViewTaskModel = TaskViewTaskModel(
        id = task.id,
        timeStart = task.startDateTime,
        timeEnd = task.endDateTime,
        color = task.color,
        taskName = task.taskName
    )
}