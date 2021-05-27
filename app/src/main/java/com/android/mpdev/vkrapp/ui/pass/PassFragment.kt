package com.android.mpdev.vkrapp.ui.pass

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentPassBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore


private const val TAG = "PassFragment"

class PassFragment : Fragment() {

    private lateinit var _binding: FragmentPassBinding
    private val binding get() = _binding

    private lateinit var viewModel: PassViewModel
    private val passViewModel: PassViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var adapter: PassFragment.RecyclerAdapter? = RecyclerAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPassBinding.inflate(inflater, container, false)
        recyclerView = binding.passRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
        recyclerView.adapter = adapter
        Log.d(TAG, "passfrag created")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PassViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        db.collection("items").addSnapshotListener(EventListener{ querySnapshot, error ->
            if(error != null){
                Log.w(TAG, "listen:error", error)
            }
            updateUI()
        })

    }

    override fun onResume() {
        super.onResume()
        passViewModel.passIsVisible = true
    }


    override fun onStop() {
        super.onStop()
        passViewModel.passIsVisible = false
        passViewModel.passIsInit = false
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
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

    private inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private lateinit var product: Product

        private val productName: TextView = itemView.findViewById(R.id.product_name)
        //private val productImg: ImageView = itemView.findViewById(R.id.product_img)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            this.product = product
            productName.text = product.id
            productPrice.text = product.price + "₽"
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
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val product = products[position]
            holder.bind(product)
        }

        override fun getItemCount() = products.size
    }
}
/*

//write
// Create a new user with a first, middle, and last name
// Create a new user with a first, middle, and last name

val user: MutableMap<String, Any> = HashMap()
user["first"] = "Alan"
user["middle"] = "Mathison"
user["last"] = "Turing"
user["born"] = 1912

// Add a new document with a generated ID

// Add a new document with a generated ID
db.collection("users")
    .add(user)
    .addOnSuccessListener { documentReference ->
        Log.d(
            TAG,
            "DocumentSnapshot added with ID: " + documentReference.id
        )
    }
    .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
 */
