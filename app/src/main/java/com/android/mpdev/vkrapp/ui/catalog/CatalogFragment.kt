package com.android.mpdev.vkrapp.ui.catalog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentCatalogBinding
import com.android.mpdev.vkrapp.ui.pass.Product
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "CatalogFragment"

class CatalogFragment : Fragment() {

    private lateinit var _binding: FragmentCatalogBinding
    private val binding get() = _binding

    private lateinit var prodRecyclerView: RecyclerView
    private var prodAdapter: ProdRecycler? = ProdRecycler(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)

        //ресайкл промо
        val recyclerView: RecyclerView = binding.catalogPromo
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = RecyclerAdapter(fillList())

        //ресайкл с товарами
        prodRecyclerView = binding.catalogRv
        prodRecyclerView.layoutManager =
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        val db = FirebaseFirestore.getInstance()
        val products = mutableListOf<Product>()

        //Читаем FireStore
        db.collection("items")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val product = Product()
                        product.id = document.id
                        product.price = document.get("price").toString()
                        product.count = document.get("count").toString()
                        products += product
                    }
                    prodAdapter = ProdRecycler(products)
                    prodRecyclerView.adapter = prodAdapter

                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private class RecyclerAdapter(private val images: List<Image>) :
        RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

        data class Image(val imageSrc: Int)

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val catalogImg: ImageView =  itemView.findViewById(R.id.catalog_img)

            fun bindView(image: Image){
                catalogImg.setImageResource(image.imageSrc)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.catalog_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindView(images[position])
        }

        override fun getItemCount(): Int {
            return images.size
        }
    }

    private fun fillList(): List<RecyclerAdapter.Image> {
        return listOf(
            RecyclerAdapter.Image(R.mipmap.cat_promo2),
            RecyclerAdapter.Image(R.mipmap.cat_promo3),
            RecyclerAdapter.Image(R.mipmap.cat_promo1),
            RecyclerAdapter.Image(R.mipmap.cat_promo6),
            RecyclerAdapter.Image(R.mipmap.cat_promo4),
            RecyclerAdapter.Image(R.mipmap.cat_promo5)
        )
    }

    private class ProdRecycler(private var products: List<Product>) :
        RecyclerView.Adapter<ProdRecycler.MyViewHolder>() {
        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private lateinit var product: Product

            val catalogImg: ImageView = itemView.findViewById(R.id.cat_prod_img)
            val catProdTv: TextView = itemView.findViewById(R.id.cat_prod_tv)
            val catProdPrice: TextView = itemView.findViewById(R.id.cat_prod_price)

            fun bind(product: Product) {
                this.product = product
                catProdTv.text = product.id
                catProdPrice.text = (product.price + "₽")
                when (product.id) {
                    "J7" -> {
                        catalogImg.setImageResource(R.mipmap.j7_juice)
                    }
                    "kitkat" -> {
                        catalogImg.setImageResource(R.mipmap.kitkat)
                    }
                    "lipton" -> {
                        catalogImg.setImageResource(R.mipmap.lipton)
                    }
                    "milk" -> {
                        catalogImg.setImageResource(R.mipmap.milk)
                    }
                    "nutella" -> {
                        catalogImg.setImageResource(R.mipmap.nutella)
                    }
                    "oreo" -> {
                        catalogImg.setImageResource(R.mipmap.oreo)
                    }
                    "picnic" -> {
                        catalogImg.setImageResource(R.mipmap.picnic)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.catalog_prod_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val product = products[position]
            holder.bind(product)
        }

        override fun getItemCount() = products.size
    }
}