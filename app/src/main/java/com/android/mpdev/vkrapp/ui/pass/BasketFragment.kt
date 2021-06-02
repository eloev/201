package com.android.mpdev.vkrapp.ui.pass

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.mpdev.vkrapp.databinding.FragmentBasketBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "PassFragment"

class BasketFragment : Fragment() {

    private lateinit var _binding: FragmentBasketBinding
    private val binding get() = _binding

    private val db = FirebaseFirestore.getInstance()

    private val j7 = "J7"
    private val kitkat = "kitkat"
    private val lipton = "lipton"
    private val milk = "milk"
    private val nutella = "nutella"
    private val oreo = "oreo"
    private val picnic = "picnic"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        db.collection("items").addSnapshotListener(EventListener { _, error ->
            if (error != null) {
                Log.w(TAG, "listen:error", error)
            }
            updateUI()
        })
        //кнопки плюс
        binding.plusJ7.setOnClickListener{AddProduct(j7)}
        binding.plusKitkat.setOnClickListener{AddProduct(kitkat)}
        binding.plusLipton.setOnClickListener{AddProduct(lipton)}
        binding.plusMilk.setOnClickListener{AddProduct(milk)}
        binding.plusNutella.setOnClickListener{AddProduct(nutella)}
        binding.plusOreo.setOnClickListener{AddProduct(oreo)}
        binding.plusPicnic.setOnClickListener{AddProduct(picnic)}
        //кнопки минус
        binding.minusJ7.setOnClickListener{RemoveProduct(j7)}
        binding.minusKitkat.setOnClickListener{RemoveProduct(kitkat)}
        binding.minusLipton.setOnClickListener{RemoveProduct(lipton)}
        binding.minusMilk.setOnClickListener{RemoveProduct(milk)}
        binding.minusNutella.setOnClickListener{RemoveProduct(nutella)}
        binding.minusOreo.setOnClickListener{RemoveProduct(oreo)}
        binding.minusPicnic.setOnClickListener{RemoveProduct(picnic)}
    }

    private fun updateUI() {
        //Читаем FireStore
        db.collection("items")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        when (document.id) {
                            j7 -> {
                                binding.labelJ7.text = (j7 + " x" + document.get("count").toString())
                            }
                            kitkat -> {
                                binding.labelKitkat.text = (kitkat + " x" + document.get("count").toString())
                            }
                            lipton -> {
                                binding.labelLipton.text = (lipton + " x" + document.get("count").toString())
                            }
                            milk -> {
                                binding.labelMilk.text = (milk + " x" + document.get("count").toString())
                            }
                            nutella -> {
                                binding.labelNutella.text = (nutella + " x" + document.get("count").toString())
                            }
                            oreo -> {
                                binding.labelOreo.text = (oreo + " x" + document.get("count").toString())
                            }
                            picnic -> {
                                binding.labelPicnic.text = (picnic + " x" + document.get("count").toString())
                            }
                        }
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
}