package com.example.simbirsofttasks.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simbirsofttasks.databinding.FragmentTaskListBinding
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.presentation.TaskApp
import com.example.simbirsofttasks.presentation.viewModels.ViewModelFactory
import com.example.simbirsofttasks.presentation.viewModels.TaskListViewModel
import java.time.LocalDate
import javax.inject.Inject

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding: FragmentTaskListBinding
        get() = _binding ?: throw RuntimeException("FragmentTaskListBinding is null")

    private val component by lazy { (requireActivity().application as TaskApp).component }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<TaskListViewModel> { viewModelFactory }

    private var taskList = emptyList<TaskEntity>()

    private var currentDate = LocalDate.now()

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTaskList().observe(viewLifecycleOwner) {
            taskList = it
            sortAndPassListToTaskView(taskList, currentDate)
        }

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            currentDate = LocalDate.of(year, month + 1, dayOfMonth)
            sortAndPassListToTaskView(taskList, currentDate)
        }

        binding.taskView.onTaskClickListener = { task ->
            findNavController().navigate(TaskListFragmentDirections.actionTaskListFragmentToTaskDetailsFragment(task.id))
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sortAndPassListToTaskView(list: List<TaskEntity>, date: LocalDate) {
        binding.taskView.setTaskList(list.filter { it.startDateTime.toLocalDate() == date })
    }
}