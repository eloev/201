package com.android.mpdev.vkrapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentMainBinding
import com.android.mpdev.vkrapp.ui.receipt.RecyclerAdapter

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var _binding: FragmentMainBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.main_promo_rv)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = RecyclerAdapter(fillList())
        
        val navController = NavHostFragment.findNavController(this)

        val mainBonusTv: TextView = view.findViewById(R.id.main_bonus_tv)
        val mainBonusImg: ImageView = view.findViewById(R.id.main_bonus_img)
        val mainReceiptTv: TextView = view.findViewById(R.id.main_receipt_tv)
        val mainReceiptImg: ImageView = view.findViewById(R.id.main_receipt_img)
        val mainPromoGradient: ImageView = view.findViewById(R.id.main_promo_gradient)
        val mainPromoTv: TextView = view.findViewById(R.id.main_promo_tv)
        mainBonusTv.setOnClickListener{ navController.navigate(R.id.navigation_bonus) }
        mainBonusImg.setOnClickListener{ navController.navigate(R.id.navigation_bonus) }
        mainReceiptTv.setOnClickListener{ navController.navigate(R.id.navigation_receipt) }
        mainReceiptImg.setOnClickListener{ navController.navigate(R.id.navigation_receipt) }
        mainPromoTv.text = "2+1 напитки"
        
        return view
    }

    private fun fillList(): List<com.android.mpdev.vkrapp.ui.main.RecyclerAdapter.Image> {
        val images = listOf(
            com.android.mpdev.vkrapp.ui.main.RecyclerAdapter.Image(R.mipmap.promo1),
            com.android.mpdev.vkrapp.ui.main.RecyclerAdapter.Image(R.mipmap.promo2),
            com.android.mpdev.vkrapp.ui.main.RecyclerAdapter.Image(R.mipmap.promo3),
            com.android.mpdev.vkrapp.ui.main.RecyclerAdapter.Image(R.mipmap.promo4))
        return images
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

}