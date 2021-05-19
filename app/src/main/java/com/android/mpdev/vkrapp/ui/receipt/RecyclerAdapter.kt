package com.android.mpdev.vkrapp.ui.receipt

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.ReceiptItemBinding
import java.text.SimpleDateFormat

class RecyclerAdapter(var receipts: List<Receipt>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private lateinit var _binding: ReceiptItemBinding

    private val binding get() = _binding

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var receiptItemDate: TextView? = null
        var receiptItemPrice: TextView? = null
        var receiptItemImg: ImageView? = null

        init {
            receiptItemDate = itemView.findViewById(R.id.receipt_item_date)
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var sdf = SimpleDateFormat("hh:mm, dd/MM/yyyy")
        holder.receiptItemDate?.text = (sdf.format(receipts[position].date)).toString()
        holder.receiptItemPrice?.text = (receipts[position].price).toString() + "₽"
    }

    override fun getItemCount(): Int {
        return receipts.size
    }
}