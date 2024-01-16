package com.example.simbirsofttasks.presentation.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.simbirsofttasks.R
import com.example.simbirsofttasks.data.Mapper
import com.example.simbirsofttasks.domain.model.TaskEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.properties.Delegates

typealias OnTaskClickListener = (task: TaskViewTaskModel) -> Unit

class TaskView(
    context: Context,
    attributesSet: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attributesSet, defStyleAttr, defStyleRes) {

    private var mapper = Mapper()

    private var taskList = listOf<TaskViewTaskModel>()

    private var taskRectList = listOf<Pair<TaskViewTaskModel, RectF>>()

    var onTaskClickListener: OnTaskClickListener? = null

    private var gridColor by Delegates.notNull<Int>()
    private var currentTimeColor by Delegates.notNull<Int>()
    private var primaryTaskColor by Delegates.notNull<Int>()
    private var secondaryTaskColor by Delegates.notNull<Int>()
    private var timeTextColor by Delegates.notNull<Int>()
    private var taskLightTextColor by Delegates.notNull<Int>()

    private val gridPaint = Paint()
    private val timeTextPaint = Paint()
    private val currentTimePaint = Paint()
    private val taskNamePaint = Paint()
    private val currentTaskPaint = Paint()

    private var fieldRect = RectF()

    private var rowWidth: Float = 0F
    private var rowHeight: Float = 0F

    private var currentTask: Int = -1

    private val currentTime: Int
        get() {
            val time = LocalDateTime.now()
            return time.hour * 60 + time.minute
        }

    constructor(context: Context, attributesSet: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attributesSet,
        defStyleAttr,
        R.style.DefaultTaskViewStyle
    )

    constructor(context: Context, attributesSet: AttributeSet?) : this(
        context,
        attributesSet,
        R.attr.taskViewStyle
    )

    constructor(context: Context) : this(context, null)

    init {
        if (attributesSet != null) {
            initAttributes(attributesSet, defStyleAttr, defStyleRes)
        } else {
            initDefaultColors()
        }
        initPaints()
        isFocusable = true
        isClickable = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("TaskView", "onSizeChanged")

        calculateViewSize()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("TaskView", "onMeasure")
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredRowHeightInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DESIRED_ROW_HEIGHT, resources.displayMetrics
        ).toInt()
        val desiredRowWidthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DESIRED_ROW_WIDTH, resources.displayMetrics
        ).toInt()

        val desiredHeight = max(
            desiredRowHeightInPixels * HOURS_COUNT + paddingTop + paddingBottom, minHeight
        )
        val desiredWidth = max(
            desiredRowWidthInPixels + paddingLeft + paddingRight, minWidth
        )

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawGrid(canvas)
        drawHourText(canvas)
        drawTasks(canvas)
        drawCurrentTimeLine(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                updateCurrentTask(event)
                return true
            }
            MotionEvent.ACTION_UP -> {
                return performClick()
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (currentTask != -1){
            onTaskClickListener?.invoke(taskRectList[currentTask].first)
            return true
        }
        return false
    }

    private fun updateCurrentTask(event: MotionEvent){
        for (i in 0 until taskRectList.size){
            val rect = taskRectList[i].second

            if (rect.contains(event.x, event.y)){
                currentTask = i
            }
        }
    }

    private fun drawGrid(canvas: Canvas) {
        val xStart = fieldRect.left + HORIZONTAL_LINE_OFFSET
        val xEnd = fieldRect.right
        val yStart = fieldRect.top
        val yEnd = fieldRect.bottom

        for (i in 1 until HOURS_COUNT) {
            val y = fieldRect.top + rowHeight * i
            canvas.drawLine(xStart, y, xEnd, y, gridPaint)
        }

        canvas.drawLine(
            fieldRect.left + VERTICAL_LINE_OFFSET,
            yStart,
            fieldRect.left + VERTICAL_LINE_OFFSET,
            yEnd,
            gridPaint
        )
    }

    private fun drawHourText(canvas: Canvas) {
        for (i in 1 until HOURS_COUNT) {
            val text = "$i:00"
            val hourY = rowHeight * i + TEXT_VERTICAL_OFFSET
            val hourX = TEXT_HORIZONTAL_OFFSET - timeTextPaint.measureText(text)
            canvas.drawText(text, hourX, hourY, timeTextPaint)
        }
    }

    private fun drawCurrentTimeLine(canvas: Canvas) {
        val xStart = fieldRect.left + VERTICAL_LINE_OFFSET
        val xEnd = fieldRect.right
        val y = fieldRect.bottom * currentTime / 1440

        canvas.drawCircle(xStart, y, 10F, currentTimePaint)
        canvas.drawLine(xStart, y, xEnd, y, currentTimePaint)
    }

    private fun drawTasks(canvas: Canvas) {
        taskRectList.forEach { taskRect ->

            val task = taskRect.first
            val rect = taskRect.second

            task.paint.pathEffect = CornerPathEffect(TASK_CORNER_RADIUS)

            task.paint.color = when(true){
                (task.color != 0) -> task.color
                (!task.isIntersecting) -> primaryTaskColor
                else -> secondaryTaskColor
            }

            val nameXStart = rect.left + TASK_NAME_START_OFFSET
            val nameYStart = rect.top + TASK_NAME_TOP_OFFSET

            val timeText = getTimeString(task)
            val timeXStart = nameXStart + taskNamePaint.measureText(task.taskName) + TASK_TEXT_SPACE

            canvas.drawRect(rect, task.paint)
            canvas.drawText(task.taskName, nameXStart, nameYStart, taskNamePaint)
            canvas.drawText(timeText, timeXStart, nameYStart, taskNamePaint)
        }
    }

    private fun calculateViewSize() {
        val safeWidth = width - paddingLeft - paddingRight
        val safeHeight = height - paddingTop - paddingBottom

        rowWidth = safeWidth.toFloat()
        rowHeight = safeHeight / HOURS_COUNT.toFloat()

        val fieldWidth = rowWidth
        val fieldHeight = rowHeight * HOURS_COUNT

        fieldRect.left = paddingLeft.toFloat()
        fieldRect.top = paddingTop.toFloat()
        fieldRect.right = fieldRect.left + fieldWidth
        fieldRect.bottom = fieldRect.top + fieldHeight
    }

    private fun calculateTaskCoords() {
        val taskRect: MutableList<Pair<TaskViewTaskModel, RectF>> = mutableListOf()
        taskList.forEach { task ->
            val xStart =
                if (task.isIntersecting) VERTICAL_LINE_OFFSET + TASK_RECT_START_OFFSET + TASK_RECT_OVERLAPPING_START_OFFSET
                else VERTICAL_LINE_OFFSET + TASK_RECT_START_OFFSET

            val yStart = fieldRect.bottom * task.getStartTime() / 1440

            val xEnd =
                if (task.isIntersecting) fieldRect.right - TASK_RECT_END_OFFSET - TASK_RECT_OVERLAPPING_END_OFFSET
                else fieldRect.right - TASK_RECT_END_OFFSET

            val expectedYEnd = fieldRect.bottom * task.getEndTime() / 1440

            val yEnd =
                if (expectedYEnd - yStart > TASK_RECT_MIN_HEIGHT) expectedYEnd else yStart + TASK_RECT_MIN_HEIGHT

            val rect = RectF(xStart, yStart, xEnd, yEnd)
            taskRect.add(Pair(task, rect))
        }
        taskRectList = taskRect
    }

    private fun initPaints() {
        with(gridPaint) {
            color = gridColor
            style = Paint.Style.STROKE
            strokeWidth = 1F
        }
        with(timeTextPaint) {
            color = timeTextColor
            textSize = 35F
            isAntiAlias = true
        }
        with(taskNamePaint) {
            color = taskLightTextColor
            textSize = 36F
            isAntiAlias = true
        }
        with(currentTimePaint) {
            color = currentTimeColor
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 1F
        }
        with(currentTaskPaint){
            color = gridColor
            style = Paint.Style.STROKE
            strokeWidth = 2F
            isAntiAlias = true
        }
    }

    private fun initAttributes(attributesSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attributesSet, R.styleable.TaskView, defStyleAttr, defStyleRes)

        currentTimeColor = typedArray.getColor(R.styleable.TaskView_currentTimeColor, DEFAULT_CURRENT_TIME_COLOR)
        primaryTaskColor = typedArray.getColor(R.styleable.TaskView_primaryTaskColor, DEFAULT_PRIMARY_TASK_COLOR)
        secondaryTaskColor = typedArray.getColor(R.styleable.TaskView_secondaryTaskColor, DEFAULT_SECONDARY_TASK_COLOR)
        timeTextColor = typedArray.getColor(R.styleable.TaskView_timeTextColor, DEFAULT_TIME_TEXT_COLOR)
        taskLightTextColor = typedArray.getColor(R.styleable.TaskView_taskLightTextColor, DEFAULT_TASK_TEXT_COLOR)
        gridColor = typedArray.getColor(R.styleable.TaskView_gridColor, DEFAULT_GRID_COLOR)

        typedArray.recycle()
    }

    fun setTaskList(list: List<TaskEntity>){
        taskList = list.map { mapper.entityToTaskViewTaskModel(it) }.toMutableList()
        sortRects()
        checkIntersect()
        calculateTaskCoords()
        requestLayout()
        invalidate()
    }

    private fun initDefaultColors() {
        currentTimeColor = context.getColor(R.color.md_theme_dark_onBackground)
        primaryTaskColor = context.getColor(R.color.md_theme_dark_primaryContainer)
        secondaryTaskColor = context.getColor(R.color.md_theme_dark_secondaryContainer)
        timeTextColor = context.getColor(R.color.md_theme_dark_outline)
        taskLightTextColor = context.getColor(R.color.md_theme_dark_onBackground)
        gridColor = context.getColor(R.color.md_theme_dark_outline)
    }

    private fun getTimeString(task: TaskViewTaskModel): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return task.timeStart.format(formatter) + " - " + task.timeEnd.format(formatter)
    }

    private fun sortRects() {
        taskList = taskList.sortedBy { it.timeStart }
    }

    private fun checkIntersect() {
        for (i in taskList.indices) {
            val currentTask = taskList[i]
            for (j in 0 until i) {
                val previousTask = taskList[j]
                if (currentTask != previousTask && isTimeOverlap(currentTask, previousTask)) {
                    currentTask.isIntersecting = true
                }
            }
        }
    }

    private fun isTimeOverlap(currentTask: TaskViewTaskModel, previousTask: TaskViewTaskModel): Boolean {
        return (currentTask.timeStart.isAfter(previousTask.timeStart) && currentTask.timeEnd.isBefore(
            previousTask.timeEnd
        )) || (currentTask.timeStart.isBefore(previousTask.timeEnd) && currentTask.timeEnd.isAfter(
            previousTask.timeEnd
        ))
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val taskViewState = state as? TaskViewState
        super.onRestoreInstanceState(taskViewState?.superState ?: state)
        taskList = taskViewState?.taskList ?: listOf()
        fieldRect = taskViewState?.fieldRect ?: RectF()
        calculateTaskCoords()
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return TaskViewState(superState, taskList, fieldRect)
    }

    companion object {
        private const val DESIRED_ROW_HEIGHT = 50F
        private const val DESIRED_ROW_WIDTH = 200F
        private const val HOURS_COUNT = 24
        private const val VERTICAL_LINE_OFFSET = 150F
        private const val HORIZONTAL_LINE_OFFSET = 120F
        private const val TEXT_HORIZONTAL_OFFSET = 110F
        private const val TEXT_VERTICAL_OFFSET = 10F
        private const val TASK_RECT_START_OFFSET = 20F
        private const val TASK_RECT_END_OFFSET = 30F
        private const val TASK_RECT_MIN_HEIGHT = 80F
        private const val TASK_RECT_OVERLAPPING_START_OFFSET = 20F
        private const val TASK_RECT_OVERLAPPING_END_OFFSET = 10F
        private const val TASK_NAME_START_OFFSET = 25F
        private const val TASK_NAME_TOP_OFFSET = 50F
        private const val TASK_TEXT_SPACE = 10F
        private const val TASK_CORNER_RADIUS = 32F

        private const val DEFAULT_CURRENT_TIME_COLOR = Color.WHITE
        private const val DEFAULT_PRIMARY_TASK_COLOR = Color.RED
        private const val DEFAULT_SECONDARY_TASK_COLOR = Color.YELLOW
        private const val DEFAULT_TIME_TEXT_COLOR = Color.LTGRAY
        private const val DEFAULT_TASK_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_GRID_COLOR = Color.LTGRAY
    }
}