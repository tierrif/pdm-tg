package com.example.pdm_tg.ui.tasklistlist

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentTaskListListBinding
import com.example.pdm_tg.db.TaskList
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TaskListListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListListBinding

    private val viewModel: TaskListListViewModel by viewModels()
    private val adapter = TaskListAdapter(::onListClick, ::onListLongClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTaskListListBinding.inflate(inflater).also {
        binding = it
    }.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // https://developer.android.com/guide/fragments/animate#kotlin
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_out)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Render the RecyclerView.
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            val taskLists = viewModel.getTaskLists()

            taskLists.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        // Handle the FAB.
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.newTodoOrList)
        fab.setOnClickListener {
            findNavController().navigate(
                TaskListListFragmentDirections.actionTaskListListFragmentToNewTodoFragment()
            )
        }

        // Handle the New List button.
        val newList = requireActivity().findViewById<MaterialButton>(R.id.newList)
        newList.setOnClickListener {
            findNavController().navigate(
                TaskListListFragmentDirections.actionTaskListListFragmentToNewListFragment()
            )
        }

        // Add listeners for the default lists (My Day and Planned)
        val myDay = requireActivity().findViewById<ConstraintLayout>(R.id.myDay)
        val planned = requireActivity().findViewById<ConstraintLayout>(R.id.planned)

        myDay.setOnClickListener {
            onListClick(null, ListType.MY_DAY)
        }

        myDay.setOnLongClickListener {
            onListLongClick(null, ListType.MY_DAY)
            true
        }

        planned.setOnClickListener {
            onListClick(null, ListType.PLANNED)
        }

        planned.setOnLongClickListener {
            onListLongClick(null, ListType.PLANNED)
            true
        }
    }

    /**
     * List click listener that sends the user
     * to the task list's fragment.
     *
     * @param taskList The task list if the list type is custom. Pass null
     *                 if it's a default list.
     * @param listType The list type. CUSTOM by default.
     *
     * @throws NullPointerException When listType is CUSTOM and taskList is null.
     */
    private fun onListClick(taskList: TaskList?, listType: ListType = ListType.CUSTOM) {
        val listName = when (listType) {
            ListType.CUSTOM -> taskList!!.listName // taskList cannot be null if ListType is custom.
            ListType.PLANNED -> getString(R.string.planned)
            ListType.MY_DAY -> getString(R.string.my_day)
        }

        findNavController().navigate(
            R.id.action_taskListListFragment_to_taskListFragment,
            Bundle().apply {
                putString("listName", listName)
                taskList?.id?.let { putLong("listId", it) }
                putSerializable("type", listType)
            }
        )
    }

    private fun onListLongClick(taskList: TaskList?, defaultList: ListType = ListType.CUSTOM) {
        // TODO: Delete/edit menu.
    }

    enum class ListType { MY_DAY, PLANNED, CUSTOM }
}
