package com.android.mpdev.vkrapp.ui.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.mpdev.vkrapp.R


private const val TAG = "Receipts"

class ReceiptAddFragment : Fragment() {

    private val receiptViewModel: ReceiptViewModel by activityViewModels()

    private lateinit var receipt: Receipt

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_receipt_add, container, false)
        val receiptAddPrice: TextView = view.findViewById(R.id.receipt_add_price)
        val receiptAddDate: TextView = view.findViewById(R.id.receipt_add_time)
        val receiptAddTv: TextView = view.findViewById(R.id.receipt_add_tv)

        if (receiptViewModel.receiptInitialized) {
            receipt = receiptViewModel.receipt
            receiptAddPrice.text = (getString(R.string.pass_all_price) + " " + receipt.price.toString() + " ₽ ")
            receiptAddDate.text = (getString(R.string.pass_date) + " " + receipt.date)
            receiptAddTv.text = receipt.products
        }

        return view
    }
}