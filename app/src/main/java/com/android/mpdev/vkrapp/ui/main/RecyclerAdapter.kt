package com.android.mpdev.vkrapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R

class RecyclerAdapter(private val images: List<Image>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    data class Image(val imageSrc: Int)

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainPromoImg: ImageView? = null
        init {
            mainPromoImg = itemView.findViewById(R.id.main_promo_img)
        }
        fun bindView(image: Image){
            mainPromoImg?.setImageResource(image.imageSrc)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.main_img_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}