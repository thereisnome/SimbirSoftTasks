package com.example.simbirsofttasks.presentation.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simbirsofttasks.R
import com.example.simbirsofttasks.databinding.FragmentAddTaskBinding
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.presentation.Colors
import com.example.simbirsofttasks.presentation.TaskApp
import com.example.simbirsofttasks.presentation.viewModels.ViewModelFactory
import com.example.simbirsofttasks.presentation.viewModels.AddTaskViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding: FragmentAddTaskBinding
        get() = _binding ?: throw RuntimeException("FragmentAddTaskBinding is null")

    private val component by lazy { (requireActivity().application as TaskApp).component }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<AddTaskViewModel> { viewModelFactory }

    private var color = 0
    private var startDate = LocalDate.now()
    private var startTime = LocalTime.now()
    private var startDateTime = LocalDateTime.of(startDate, startTime)
    private var endDate = LocalDate.now()
    private var endTime = LocalTime.of(startTime.hour + 1, startTime.minute)
    private var endDateTime = LocalDateTime.of(endDate, endTime)

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createChipGroup()
        setDateTimeClickListeners()
        setCurrentDateTime()

        binding.etTaskName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvTaskName.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.buttonSave.setOnClickListener {
            if (binding.etTaskName.text.toString().isBlank()) {
                binding.tvTaskName.error = requireContext().getString(R.string.input_error)
            } else {
                addTask()
                findNavController().navigateUp()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCurrentDateTime() {
        binding.startDate.text = getFormattedDate(startDate)
        binding.endDate.text = getFormattedDate(endDate)
        binding.startTime.text = getFormattedTime(startTime)
        binding.endTime.text = getFormattedTime(endTime)
    }

    private fun setDateTimeClickListeners() {
        binding.startDate.setOnClickListener {
            showStartDatePicker()
        }

        binding.endDate.setOnClickListener {
            showEndDatePicker()
        }

        binding.startTime.setOnClickListener {
            showStartTimePicker()
        }

        binding.endTime.setOnClickListener {
            showEndTimePicker()
        }
    }

    private fun showStartDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
        datePicker.addOnPositiveButtonClickListener { dateInMillis ->
            startDate = createLocalDateFromMillis(dateInMillis)
            endDate = createLocalDateFromMillis(dateInMillis)
            Log.d("TAG", dateInMillis.toString())
            binding.startDate.text = getFormattedDate(startDate)
            binding.endDate.text = getFormattedDate(endDate)
        }
        datePicker.show(childFragmentManager, "datePicker")
    }

    private fun showStartTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(startTime.hour)
                .setMinute(startTime.minute).setTitleText("Select time").build()
        timePicker.addOnPositiveButtonClickListener {
            startTime = LocalTime.of(timePicker.hour, timePicker.minute)
            endTime = LocalTime.of(timePicker.hour + 1, timePicker.minute)
            binding.startTime.text = getFormattedTime(startTime)
            binding.endTime.text = getFormattedTime(endTime)
            startDateTime = LocalDateTime.of(startDate, startTime)
            endDateTime = LocalDateTime.of(endDate, endTime)
        }
        timePicker.show(childFragmentManager, "timePicker")
    }

    private fun showEndDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
        datePicker.addOnPositiveButtonClickListener { dateInMillis ->
            endDate = createLocalDateFromMillis(dateInMillis)
            Log.d("TAG", dateInMillis.toString())
            binding.endDate.text = getFormattedDate(endDate)
        }
        datePicker.show(childFragmentManager, "datePicker")
    }

    private fun showEndTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(endTime.hour)
                .setMinute(endTime.minute).setTitleText("Select time").build()
        timePicker.addOnPositiveButtonClickListener {
            if (getTimeInMinutes(
                    timePicker.hour, timePicker.minute
                ) < getTimeInMinutes(startTime.hour, startTime.minute)
            ) {
                Toast.makeText(requireContext(), "Please pick correct time", Toast.LENGTH_SHORT)
                    .show()
                endTime = LocalTime.of(startTime.hour + 1, startTime.minute)
                binding.endTime.text = getFormattedTime(endTime)
            } else {
                endTime = LocalTime.of(timePicker.hour, timePicker.minute)
                binding.endTime.text = getFormattedTime(endTime)
                endDateTime = LocalDateTime.of(endDate, endTime)
            }
        }
        timePicker.show(childFragmentManager, "timePicker")
    }

    private fun createLocalDateFromMillis(timeInMillis: Long): LocalDate {
        val instant = Instant.ofEpochMilli(timeInMillis)
        val zoneId = ZoneId.systemDefault()

        return instant.atZone(zoneId).toLocalDate()
    }

    private fun getTimeInMinutes(hour: Int, minute: Int): Int = hour * 60 + minute

    private fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy")
        return formatter.format(date)
    }

    private fun getFormattedTime(localTime: LocalTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return formatter.format(LocalTime.of(localTime.hour, localTime.minute))
    }

    private fun addTask() {
        val taskName = binding.etTaskName.text.toString()
        val taskDesc = binding.etTaskDescription.text.toString().ifBlank { "" }

        val task = TaskEntity(
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            color = color,
            taskName = taskName,
            taskDesc = taskDesc
        )

        viewModel.addTask(task)
    }

    private fun createChipGroup() {
        val colors = Colors.entries
        colors.forEach { color ->
            val chip = Chip(requireContext())
            chip.chipStrokeWidth = 0f
            chip.chipBackgroundColor = ColorStateList.valueOf(color.hex.toColorInt())
            binding.chipGroupColor.addView(chip)
            chip.setOnClickListener {
                this.color = color.hex.toColorInt()
            }
        }
    }
}