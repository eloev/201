package com.android.mpdev.vkrapp.ui.bonus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentBonusBinding
import com.android.mpdev.vkrapp.ui.receipt.Receipt
import com.android.mpdev.vkrapp.ui.receipt.ReceiptViewModel

class BonusFragment : Fragment() {

    private lateinit var _binding: FragmentBonusBinding
    private val binding get() = _binding

    private val receiptViewModel: ReceiptViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var adapter: RecyclerAdapter? = RecyclerAdapter(emptyList())

    private var allBonus: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBonusBinding.inflate(inflater, container, false)
        recyclerView = binding.bonusRecyclerView
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
            { receipts -> receipts?.let { updateUI(receipts) } }
        )
    }

    private fun updateUI(receipts: List<Receipt>) {
        adapter = RecyclerAdapter(receipts)
        recyclerView.adapter = adapter
    }

    fun bonusCount(allBonus: Int) {
        binding.bonusAll.text =
            (getString(R.string.bonus_all) + "  " + allBonus.toString() + "  " + getString(R.string.bonus_counter))
    }

    private inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var receipt: Receipt

        private val bonusItemDate: TextView = itemView.findViewById(R.id.bonus_item_date)
        private val bonusItem: TextView = itemView.findViewById(R.id.bonus_item)


        fun bind(receipt: Receipt) {
            this.receipt = receipt
            bonusItemDate.text = receipt.date
            bonusItem.text = (" ${receipt.price / 10}")
            allBonus += receipt.price / 10
            bonusCount(allBonus)
        }
    }

    private inner class RecyclerAdapter(private var receipts: List<Receipt>) :
        RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.bonus_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val receipt = receipts[position]
            holder.bind(receipt)
        }

        override fun getItemCount() = receipts.size
    }
}