package com.example.pdm_tg.ui.newlist

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pdm_tg.InheritableFragment
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentNewTaskListBinding
import com.example.pdm_tg.db.TaskList
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class NewTaskListFragment : InheritableFragment<TaskList>() {
    private lateinit var binding: FragmentNewTaskListBinding
    private val viewModel: NewTaskListViewModel by viewModels()

    protected lateinit var name: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewTaskListBinding.inflate(inflater).also {
        binding = it
    }.root

    /**
     * To inherit by the details class to fill
     * fields with data already stored in the
     * db for this task in specific.
     */
    override fun fillFields() = Unit

    /**
     * Called when the user saves successfully.
     * This won't be called if there is an input error.
     * This should only be implemented by the edit fragment.
     *
     * @param t The task list to save.
     */
    override fun onSave(t: TaskList): Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // https://developer.android.com/guide/fragments/animate#kotlin
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_in)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // List creation.
        val saveBtn = requireActivity().findViewById<MaterialButton>(R.id.save)
        name = requireActivity().findViewById(R.id.listNameEditText)

        fillFields()

        saveBtn.setOnClickListener {
            if (name.text.isNullOrEmpty()) return@setOnClickListener Toast.makeText(
                requireContext(),
                R.string.listNameEmpty,
                Toast.LENGTH_SHORT
            ).show()

            lifecycleScope.launch {
                if (viewModel.getTaskListByName(name.text.toString()).await() !== null) {
                    return@launch Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.listAlreadyExists),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (isDetails) {
                    onSave(TaskList(name.text.toString())).join()
                } else {
                    viewModel.newTaskList(name.text.toString()).join()
                }

                findNavController().popBackStack()
            }
        }
    }
}
