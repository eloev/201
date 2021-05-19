package com.android.mpdev.vkrapp.ui.receipt

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentReceiptAddBinding
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "Receipts"

class ReceiptAddFragment : Fragment() {

    private lateinit var _binding: FragmentReceiptAddBinding

    private val binding get() = _binding

    private lateinit var viewModel: ReceiptAddViewModel
    private lateinit var receipt: Receipt
    private lateinit var price: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_receipt_add, container, false)
        val receiptAddPrice: EditText = view.findViewById(R.id.receipt_add_price)
        val receiptAddDate: EditText = view.findViewById(R.id.receipt_add_time)
        val receiptAdd: Button = view.findViewById(R.id.receipt_add)

        receiptAdd.setOnClickListener {
            price = receiptAddPrice.text.toString()
            receiptAddPrice.text = null
            receiptAddPrice.clearFocus()
            updateUI(price)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReceiptAddViewModel::class.java)
    }

    override fun onStop() {
        super.onStop()

    }

    private fun updateUI(price: String){
        var nReceipt = Receipt(UUID.randomUUID(), Date(), price.toInt())
        viewModel.addReceipt(nReceipt)
        Log.d(TAG, "$nReceipt")
    }

}