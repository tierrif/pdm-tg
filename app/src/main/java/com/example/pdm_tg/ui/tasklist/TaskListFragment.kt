package com.example.pdm_tg.ui.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentTaskListBinding
import com.example.pdm_tg.db.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var binding: FragmentTaskListBinding
    private val args: TaskListFragmentArgs by navArgs()
    private val adapter = TaskAdapter(::onTaskClick, ::onTaskStatusChange)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTaskListBinding.inflate(inflater).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Render the RecyclerView.
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            // The view model's getTaskList method already filters based on the list chosen.
            val tasks = viewModel.getTaskList()

            tasks.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        val addTaskButton = requireActivity().findViewById<FloatingActionButton>(R.id.newTodo)
        addTaskButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_taskListFragment_to_newTaskFragment,
                Bundle().apply {
                    putLong("taskListId", args.listId)
                }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        // Display the screen's title as the list name.
        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.listName
    }

    /**
     * Fired when a task is clicked. Takes
     * the user to the task edit fragment
     * to allow them to edit it.
     *
     * @param task The task the user clicked.
     */
    private fun onTaskClick(task: Task) {
        findNavController().navigate(
            R.id.action_taskListFragment_to_taskEditFragment,
            Bundle().apply {
                putLong("taskId", task.id)
            }
        )
    }

    /**
     * Fired when the user checks/unchecks
     * the checkbox for the task in the list.
     *
     * Updates the task and sets it to done/not done according
     * to the task info provided in the param.
     *
     * @param task The task the user changed the status for.
     */
    private fun onTaskStatusChange(task: Task) {
        lifecycleScope.launch {
            viewModel.updateTask(task)
        }
    }
}
