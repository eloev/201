package com.android.mpdev.vkrapp.ui.pass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentPassBinding

private const val TAG = "Pass"

class PassFragment : Fragment() {

    private lateinit var viewModel: PassViewModel

    private val passViewModel: PassViewModel by activityViewModels()

    private lateinit var _binding: FragmentPassBinding

    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pass, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PassViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        passViewModel.passIsVisible = true
    }


    override fun onStop() {
        super.onStop()
        passViewModel.passIsVisible = false
    }
}