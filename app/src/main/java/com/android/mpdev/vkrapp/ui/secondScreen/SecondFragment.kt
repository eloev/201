package com.android.mpdev.vkrapp.ui.secondScreen

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private val writeViewModel: SecondViewModel by activityViewModels()

    private lateinit var _binding: FragmentSecondBinding

    private val binding get() = _binding

    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        initUi()
        startObservers()
        return binding.root
    }

    private fun initUi() {
        context?.let {
            dialog = Dialog(it)
        } ?: throw IllegalStateException("Сообщение не может быть нулевым")

        binding.saveTagButtonWrapper.setOnClickListener  {
            showDialog()
            writeViewModel.messageToSave = binding.messageToSave.text!!.toString()
            writeViewModel.isWriteTagOptionOn = true
        }
    }

    private fun startObservers() {
        val textView: TextView = binding.textWrite

        writeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        writeViewModel.closeDialog.observe(viewLifecycleOwner, Observer {
            if(it && dialog.isShowing == true){
                dialog.dismiss()
            }
        })
    }

    private fun showDialog() {
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_write_tag)
        dialog.show()
    }
}