package com.example.simbirsofttasks.presentation.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simbirsofttasks.R
import com.example.simbirsofttasks.databinding.FragmentTaskDetailsBinding
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.presentation.TaskApp
import com.example.simbirsofttasks.presentation.viewModels.ViewModelFactory
import com.example.simbirsofttasks.presentation.viewModels.TaskDetailsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null

    private val args by navArgs<TaskDetailsFragmentArgs>()
    private val binding: FragmentTaskDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentTaskDetailsBinding is null")

    private val component by lazy { (requireActivity().application as TaskApp).component }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<TaskDetailsViewModel> { viewModelFactory }

    private lateinit var task: TaskEntity

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTaskById(args.taskId)
        viewModel.task.observe(viewLifecycleOwner){
            task = it
            setInfo()
        }

        binding.buttonDelete.setOnClickListener {
            createDeleteDialogAlert(task)
        }
    }

    private fun setInfo(){
        binding.tvDetailsTaskName.text = task.taskName
        binding.tvDetailsTaskDescription.text = if (task.taskDesc == "") "No description" else task.taskDesc
        binding.tvDetailsStartDateTime.text = formatLocalDateTime(task.startDateTime)
        binding.tvDetailsEndDateTime.text = formatLocalDateTime(task.endDateTime)
        binding.colorCircle.backgroundTintList = ColorStateList.valueOf(task.color)
    }

    private fun createDeleteDialogAlert(task: TaskEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(
                R.string.delete_task_title,
                task.taskName
            ))
            .setMessage(resources.getString(R.string.delete_task_dialog_description))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                viewModel.removeTask(task.id)
                findNavController().navigateUp()
                Toast.makeText(requireContext(), "Task removed", Toast.LENGTH_LONG).show()
            }
            .show()
    }

    private fun formatLocalDateTime(dateTime: LocalDateTime): String{
        val formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy - HH:mm")
        return dateTime.format(formatter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}