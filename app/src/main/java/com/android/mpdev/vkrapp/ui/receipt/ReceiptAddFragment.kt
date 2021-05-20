package com.android.mpdev.vkrapp.ui.receipt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.mpdev.vkrapp.R
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
        val receiptEdit: Button = view.findViewById(R.id.receipt_edit)
        val receiptAllDelete: Button = view.findViewById(R.id.receipt_all_delete)

        if (receiptViewModel.receiptInitialized) {
            receipt = receiptViewModel.receipt
            receiptAddPrice.setText(receipt.price.toString())
            receiptAddDate.setText(receipt.date)
        }

        receiptAdd.setOnClickListener {
            if (receiptAddPrice.text.isBlank()) {
                Toast.makeText(activity, getString(R.string.null_error), LENGTH_SHORT).show()
            } else if (receiptViewModel.receiptInitialized) {
                Toast.makeText(activity, getString(R.string.added_error), LENGTH_SHORT).show()
            } else {
                price = receiptAddPrice.text.toString()
                receiptAddPrice.text = null
                receiptAddPrice.clearFocus()
                updateUI(price)
            }
        }

        receiptEdit.setOnClickListener {
            if (receiptAddPrice.text.isBlank() && receiptAddDate.text.isBlank()) {
                Toast.makeText(activity, getString(R.string.null_error), LENGTH_SHORT).show()
            } else if (!receiptViewModel.receiptInitialized) {
                Toast.makeText(activity, getString(R.string.edit_error), LENGTH_SHORT).show()
            } else {
                receipt.date = receiptAddDate.text.toString()
                receipt.price = receiptAddPrice.text.toString().toInt()
                receiptViewModel.saveReceipt(receipt)
                receiptAddPrice.text = null
                receiptAddPrice.clearFocus()
                receiptEdit.text = null
                receiptEdit.clearFocus()
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

    @SuppressLint("SimpleDateFormat")
    private fun updateUI(price: String) {
        val sdf = SimpleDateFormat("HH:mm, dd.MM.yyyy")
        val dateNow = sdf.format(Date())
        val nReceipt = Receipt(UUID.randomUUID(), dateNow, price.toInt())
        viewModel.addReceipt(nReceipt)
        Log.d(TAG, "$nReceipt")
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}