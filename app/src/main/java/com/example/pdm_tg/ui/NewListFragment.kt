package com.example.pdm_tg.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pdm_tg.databinding.FragmentNewListBinding

class NewListFragment : Fragment() {
    private lateinit var binding: FragmentNewListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewListBinding.inflate(inflater).also {
        binding = it
    }.root
}
