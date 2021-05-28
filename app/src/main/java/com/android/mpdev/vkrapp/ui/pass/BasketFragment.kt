package com.android.mpdev.vkrapp.ui.pass

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.mpdev.vkrapp.databinding.FragmentBasketBinding
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "PassFragment"

class BasketFragment : Fragment() {

    private lateinit var _binding: FragmentBasketBinding
    private val binding get() = _binding

    private lateinit var viewModel: PassViewModel
    private val passViewModel: PassViewModel by activityViewModels()

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        readDB()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PassViewModel::class.java)
    }

    private fun readDB() {
        val prodWrite: MutableMap<String, String> = HashMap()

        //Читаем FireStore
        db.collection("items")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if(document.id == passViewModel.productId){
                            prodWrite["price"] = document.get("price").toString()
                            prodWrite["count"] = document.get("count").toString()
                            writeDB(prodWrite)
                            binding.basketTv.text = ("${binding.basketTv.text} \nParam: ${passViewModel.productId}")
                        }
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
    private fun writeDB(prodWrite: MutableMap<String, String>){
        //Записываем в FireStore с ++ количества
        prodWrite["count"] = (prodWrite["count"]?.toInt()?.plus(1)).toString()
        db.collection("items")
            .document(passViewModel.productId)
            .set(prodWrite)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
        binding.basketTv.text = ("${binding.basketTv.text} \n$prodWrite")
    }
}