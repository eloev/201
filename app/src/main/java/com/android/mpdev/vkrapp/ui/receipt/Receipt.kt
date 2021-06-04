package com.android.mpdev.vkrapp.ui.receipt

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Receipt(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var date: String = "",
    var price: Int = 200,
    var products : String = ""
)