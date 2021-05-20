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
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.activityViewModels
import com.android.mpdev.vkrapp.R
import com.android.mpdev.vkrapp.databinding.FragmentReceiptAddBinding
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "Receipts"

class ReceiptAddFragment : Fragment() {

    private lateinit var viewModel: ReceiptViewModel
    private val receiptViewModel: ReceiptViewModel by activityViewModels()

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
        val receiptAllDelete: Button = view.findViewById(R.id.receipt_all_delete)

        if(receiptViewModel.receiptInitialized){
            receipt = receiptViewModel.receipt
            receiptAddPrice.setText(receipt.price.toString())
            receiptAddDate.setText(receipt.date)
        }

        receiptAdd.setOnClickListener {
            if (receiptAddPrice.text.isBlank()) {
                Toast.makeText(activity, getString(R.string.null_error), LENGTH_SHORT).show()
            } else {
                price = receiptAddPrice.text.toString()
                receiptAddPrice.text = null
                receiptAddPrice.clearFocus()
                updateUI(price)
            }
        }
        receiptAllDelete.setOnClickListener {
            receiptAddPrice.text = null
            receiptAddPrice.clearFocus()
            receiptAddDate.text = null
            receiptAddDate.clearFocus()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReceiptViewModel::class.java)
    }

    private fun updateUI(price: String) {
        val sdf = SimpleDateFormat("HH:mm, dd.MM.yyyy")
        val dateNow = sdf.format(Date())
        var nReceipt = Receipt(UUID.randomUUID(), dateNow, price.toInt())
        viewModel.addReceipt(nReceipt)
        Log.d(TAG, "$nReceipt")
    }
}