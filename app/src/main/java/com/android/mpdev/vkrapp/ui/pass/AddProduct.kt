package com.android.mpdev.vkrapp.ui.pass

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "PassFragment"

class AddProduct(private var productId: String) {
    private val db = FirebaseFirestore.getInstance()

    init {
        val prodWrite: MutableMap<String, String> = HashMap()
        //Читаем FireStore
        db.collection("items")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if(document.id == productId){
                            prodWrite["price"] = document.get("price").toString()
                            prodWrite["count"] = document.get("count").toString()
                            writeDB(prodWrite)
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
            .document(productId)
            .set(prodWrite)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }
}