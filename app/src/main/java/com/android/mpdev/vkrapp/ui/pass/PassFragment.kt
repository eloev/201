package com.android.mpdev.vkrapp.ui.pass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentPassBinding
import com.android.mpdev.vkrapp.ui.receipt.ReceiptViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "PassFragment"

class PassFragment : Fragment() {

    private lateinit var _binding: FragmentPassBinding
    private val binding get() = _binding

    private val receiptViewModel: ReceiptViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var adapter: RecyclerAdapter? = RecyclerAdapter(emptyList())

    private var allPrice = 0
    private var allProduct = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPassBinding.inflate(inflater, container, false)
        recyclerView = binding.passRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
        recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        db.collection("items").addSnapshotListener(EventListener{ _, error ->
            if(error != null){
                Log.w(TAG, "listen:error", error)
            }
            updateUI()
        })

    }

    override fun onResume() {
        super.onResume()
        receiptViewModel.passIsVisible = true
    }


    override fun onStop() {
        super.onStop()
        receiptViewModel.passIsVisible = false
        receiptViewModel.passIsInit = false
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
                        if(document.get("count").toString() != "0"){
                            product.id = document.id
                            product.price = document.get("price").toString()
                            product.count = document.get("count").toString()
                            products += product
                        }
                    }
                    adapter = RecyclerAdapter(products)
                    recyclerView.adapter = adapter
                    allPrice = 0

                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private lateinit var product: Product

        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productImg: ImageView = itemView.findViewById(R.id.product_img)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(product: Product) {
            this.product = product
            productName.text = (product.id + " x" + product.count)
            val pPrice = product.price.toInt() * product.count.toInt()
            allPrice += pPrice
            productPrice.text = (pPrice.toString() + "₽")
            allProduct += product.id + " x" + product.count + " " + pPrice.toString() + "₽" + "\n"
            binding.passAllPrice.text = (getString(R.string.pass_all_price) + " " + allPrice + " ₽")
            when (product.id) {
                "J7" -> {
                    productImg.setImageResource(R.mipmap.j7_juice)
                }
                "kitkat" -> {
                    productImg.setImageResource(R.mipmap.kitkat)
                }
                "lipton" -> {
                    productImg.setImageResource(R.mipmap.lipton)
                }
                "milk" -> {
                    productImg.setImageResource(R.mipmap.milk)
                }
                "nutella" -> {
                    productImg.setImageResource(R.mipmap.nutella)
                }
                "oreo" -> {
                    productImg.setImageResource(R.mipmap.oreo)
                }
                "picnic" -> {
                    productImg.setImageResource(R.mipmap.picnic)
                }
            }
            receiptViewModel.passProduct = allProduct
            receiptViewModel.passPrice = allPrice.toString()
        }

        override fun onClick(v: View) {
        }
    }

    private inner class RecyclerAdapter(private var products: List<Product>) :
        RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_item, parent, false)
            binding.textPass.visibility = View.GONE
            binding.progressBarPass.visibility = View.GONE
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val product = products[position]
            holder.bind(product)
        }

        override fun getItemCount() = products.size
    }
}
