package com.example.simbirsofttasks.presentation.customView

import android.graphics.Paint
import java.time.LocalDateTime

data class TaskViewTaskModel(
    val id: Long,
    val timeStart: LocalDateTime,
    val timeEnd: LocalDateTime,
    var color: Int = 0,
    val taskName: String,
    val paint: Paint = Paint(),
    var isIntersecting: Boolean = false
) {

    fun getStartTime(): Int{
        val hours = timeStart.hour
        val minutes = timeStart.minute
        return hours * 60 + minutes
    }

    fun getEndTime(): Int{
        val hours = timeEnd.hour
        val minutes = timeEnd.minute
        return hours * 60 + minutes
    }

    init {
        paint.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }
}