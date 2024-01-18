package com.example.pdm_tg.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle the FAB.
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.newTodoOrList)
        fab.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToNewTodoFragment()
            )
        }

        // Handle the New List button.
        val newList = requireActivity().findViewById<MaterialButton>(R.id.newList)
        newList.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToNewListFragment()
            )
        }
    }
}
