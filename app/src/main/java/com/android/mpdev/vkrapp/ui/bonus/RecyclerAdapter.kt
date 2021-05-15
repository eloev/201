package com.android.mpdev.vkrapp.ui.bonus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R

class RecyclerAdapter (private val names: List<String>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>()
{

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var bonusItemTv: TextView? = null
        var bonusItemPrice: TextView? = null
        var bonusItemImg: ImageView? = null
        var bonusItemOld: TextView? = null

        init {
            bonusItemTv = itemView.findViewById(R.id.bonus_item_tv)
            bonusItemPrice = itemView.findViewById(R.id.bonus_item_price)
            bonusItemImg = itemView.findViewById(R.id.bonus_item_img)
            bonusItemOld = itemView.findViewById(R.id.bonus_old_item_price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bonus_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bonusItemTv?.text = names[position]
        holder.bonusItemPrice?.text = "150"
        holder.bonusItemOld?.text = "120"
    }

    override fun getItemCount(): Int {
        return names.size
    }
}