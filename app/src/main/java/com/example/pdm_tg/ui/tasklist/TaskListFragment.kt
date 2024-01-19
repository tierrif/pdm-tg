package com.example.pdm_tg.ui.tasklist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pdm_tg.databinding.FragmentTaskListBinding
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var binding: FragmentTaskListBinding
    private val args: TaskListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTaskListBinding.inflate(inflater).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        // Display the screen's title as the list name.
        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.listName
    }
}
