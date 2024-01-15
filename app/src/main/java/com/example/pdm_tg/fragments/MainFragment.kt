package com.example.pdm_tg.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private var fabOpen = false

    private lateinit var fab: FloatingActionButton
    private lateinit var newTodoFab: FloatingActionButton
    private lateinit var newListFab: FloatingActionButton

    private lateinit var openAnimation: Animation
    private lateinit var closeAnimation: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize animations.
        openAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fab_open)
        closeAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fab_close)

        // Handle the FAB menu.
        fab = requireActivity().findViewById(R.id.newTodoOrList)
        newTodoFab = requireActivity().findViewById(R.id.newTodo)
        newListFab = requireActivity().findViewById(R.id.newList)
        fab.setOnClickListener {
            if (fabOpen) showFabMenu() else hideFabMenu()
        }
    }

    private fun showFabMenu() {
        fabOpen = true
        newTodoFab.startAnimation(openAnimation)
        newTodoFab.visibility = View.VISIBLE
        newListFab.startAnimation(openAnimation)
        newListFab.visibility = View.VISIBLE
    }

    private fun hideFabMenu() {
        fabOpen = false
        newTodoFab.startAnimation(closeAnimation)
        newListFab.startAnimation(closeAnimation)
    }
}
