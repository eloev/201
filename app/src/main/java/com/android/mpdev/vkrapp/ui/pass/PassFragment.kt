package com.android.mpdev.vkrapp.ui.pass

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentFirstBinding
import com.android.mpdev.vkrapp.databinding.FragmentPassBinding
import com.android.mpdev.vkrapp.ui.firstScreen.FirstViewModel

private const val TAG = "PassActivity"

class PassFragment : Fragment() {

    companion object {
        fun newInstance() = PassFragment()
    }

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