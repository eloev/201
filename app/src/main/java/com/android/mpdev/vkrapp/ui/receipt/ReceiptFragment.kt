package com.android.mpdev.vkrapp.ui.receipt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.databinding.FragmentReceiptBinding

class ReceiptFragment : Fragment() {

    private lateinit var _binding: FragmentReceiptBinding

    private val binding get() = _binding

    private lateinit var viewModel: ReceiptViewModel
    private lateinit var recyclerView: RecyclerView

    private val receiptViewModel: ReceiptViewModel by activityViewModels()

    private var adapter: RecyclerAdapter? = RecyclerAdapter(emptyList())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptBinding.inflate(inflater, container, false)
        recyclerView = binding.receiptRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
        recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiptViewModel.receiptListLiveData.observe(
            viewLifecycleOwner,
            Observer { receipts -> receipts?.let { updateUI(receipts) } }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReceiptViewModel::class.java)
    }

    private fun updateUI(receipts: List<Receipt>){
        adapter = RecyclerAdapter(receipts)
        recyclerView.adapter = adapter
    }

}