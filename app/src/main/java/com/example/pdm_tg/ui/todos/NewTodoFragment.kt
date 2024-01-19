package com.example.pdm_tg.ui.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pdm_tg.databinding.FragmentNewTodoBinding

class NewTodoFragment : Fragment() {
    private lateinit var binding: FragmentNewTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewTodoBinding.inflate(inflater).also {
        binding = it
    }.root
}
