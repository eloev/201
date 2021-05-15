package com.android.mpdev.vkrapp.ui.receipt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.ReceiptItemBinding

class RecyclerAdapter (private val names: List<String>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>()
{

    private lateinit var _binding: ReceiptItemBinding

    private val binding get() = _binding

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var receiptItemTv: TextView? = null
        var receiptItemPrice: TextView? = null
        var receiptItemImg: ImageView? = null

        init {
            receiptItemTv = itemView.findViewById(R.id.receipt_item_tv)
            receiptItemPrice = itemView.findViewById(R.id.receipt_item_price)
            receiptItemImg = itemView.findViewById(R.id.receipt_item_img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.receipt_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.receiptItemTv?.text = names[position]
        holder.receiptItemPrice?.text = "кот"
    }

    override fun getItemCount(): Int {
        return names.size
    }
}