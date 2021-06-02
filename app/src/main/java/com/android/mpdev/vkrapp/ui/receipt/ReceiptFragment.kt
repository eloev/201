package com.android.mpdev.vkrapp.ui.receipt

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentReceiptBinding
import java.util.*

class ReceiptFragment : Fragment() {

    private lateinit var _binding: FragmentReceiptBinding
    private val binding get() = _binding

    private val receiptViewModel: ReceiptViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var adapter: RecyclerAdapter? = RecyclerAdapter(emptyList())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptBinding.inflate(inflater, container, false)
        recyclerView = binding.receiptRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
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

    private fun updateUI(receipts: List<Receipt>) {
        adapter = RecyclerAdapter(receipts)
        recyclerView.adapter = adapter
    }

    private inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var receipt: Receipt

        private val receiptItemDate: TextView = itemView.findViewById(R.id.receipt_item_date)
        //private val receiptItemId: TextView = itemView.findViewById(R.id.receipt_item_id)
        private val receiptItemPrice: TextView = itemView.findViewById(R.id.receipt_item_price)
        //private val receiptItemImg: ImageView = itemView.findViewById(R.id.receipt_item_img)

        init {
            itemView.setOnClickListener(this)
        }
        @SuppressLint("SetTextI18n")
        fun bind(receipt: Receipt){
            this.receipt = receipt
            receiptItemDate.text = receipt.date
            receiptItemPrice.text = (receipt.price).toString() + "₽"
        }

        override fun onClick(v: View) {
            itemView.findNavController().navigate(R.id.navigation_receiptAdd)
            receiptViewModel.receipt = this.receipt
            receiptViewModel.receiptInitialized = true
        }
    }

    private inner class RecyclerAdapter(private var receipts: List<Receipt>) :
        RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.receipt_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val receipt = receipts[position]
            holder.bind(receipt)
        }

        override fun getItemCount() = receipts.size
    }
}