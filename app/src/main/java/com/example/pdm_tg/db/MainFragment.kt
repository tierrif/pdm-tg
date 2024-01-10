package com.example.pdm_tg.db

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return FragmentMainBinding.inflate(inflater).also {
            binding = it
            // use arrayadapter and define an array
            val arrayAdapter: ArrayAdapter<*>
            val users = arrayOf(
                "Virat Kohli", "Rohit Sharma", "Steve Smith",
                "Kane Williamson", "Ross Taylor"
            )

            // access the listView from xml file
            val mListView = binding.mainSection
            arrayAdapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, users)
            mListView.adapter = arrayAdapter
        }.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}