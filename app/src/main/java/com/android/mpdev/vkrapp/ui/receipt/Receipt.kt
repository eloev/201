package com.android.mpdev.vkrapp.ui.receipt

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Receipt(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var price: Int = 200
) {

}