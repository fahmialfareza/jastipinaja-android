package com.dinokeylas.jastipinaja.model

import com.google.firebase.firestore.Exclude
import java.util.*

data class Product (
    @get:Exclude var productId: String = "",
    val owner: User = User(),
    val name: String = "",
    val description: String = "",
    val category: Int = 0,
    val price: Int = 0,
    val qty: Int = 0,
    val serviceFee: Int = 0,
    val discount: Int = 0,
    val imageUrl: String = "",
    val shoppingLocation: String = "",
    val addedNote: String = "",
    var shoppingDate: Date = Calendar.getInstance().time,
    val createAt: Date = Calendar.getInstance().time,
    val updateAt: Date = Calendar.getInstance().time,
    val ExpiredAt: Date = Calendar.getInstance().time
)