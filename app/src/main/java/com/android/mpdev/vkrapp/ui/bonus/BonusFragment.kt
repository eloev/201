package com.android.mpdev.vkrapp.ui.bonus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentBonusBinding
import com.android.mpdev.vkrapp.ui.bonus.RecyclerAdapter

class BonusFragment : Fragment() {

    private lateinit var _binding: FragmentBonusBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBonusBinding.inflate(inflater, container, false)
        val recyclerView: RecyclerView = binding.bonusRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RecyclerAdapter(fillList())
        return binding.root
    }

    private fun fillList(): List<String> {
        val data = mutableListOf<String>()
        (0..30).forEach { i -> data.add("\$i element") }
        return data
    }
}