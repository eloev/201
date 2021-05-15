package com.android.mpdev.vkrapp.ui.firstScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.android.mpdev.vkrapp.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private val readViewModel: FirstViewModel by activityViewModels()

    private lateinit var _binding: FragmentFirstBinding

    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val view = binding.root
        val textView: TextView = binding.textRead
        readViewModel.tag.observe(viewLifecycleOwner, Observer { textView.text = it })
        return view
    }
}