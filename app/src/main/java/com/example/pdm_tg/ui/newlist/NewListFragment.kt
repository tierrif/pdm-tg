package com.example.pdm_tg.ui.newlist

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentNewListBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class NewListFragment : Fragment() {
    private lateinit var binding: FragmentNewListBinding
    private val viewModel: NewTaskListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewListBinding.inflate(inflater).also {
        binding = it
    }.root

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
        val name = requireActivity().findViewById<TextInputEditText>(R.id.listNameEditText)
        saveBtn.setOnClickListener {
            if (name.text.isNullOrEmpty()) return@setOnClickListener Toast.makeText(
                requireContext(),
                R.string.listNameEmpty,
                Toast.LENGTH_SHORT
            ).show()

            // TODO: check if a list with this name already exists and block if it does
            viewModel.newTaskList(name.text.toString()).invokeOnCompletion {
                lifecycleScope.launch {
                    findNavController().popBackStack()
                }
            }
        }
    }
}
