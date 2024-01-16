package com.example.simbirsofttasks.presentation.customView

import android.graphics.RectF
import android.os.Parcelable
import android.view.View.BaseSavedState

class TaskViewState(
    superSavedState: Parcelable?,
    val taskList: List<TaskViewTaskModel>,
    val fieldRect: RectF
): BaseSavedState(superSavedState)