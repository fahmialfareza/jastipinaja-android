package com.dinokeylas.jastipinaja.model

import com.google.firebase.firestore.Exclude
import java.util.*

data class Product (
    @get:Exclude var productId: String = "",
    val productOwner: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: Int = 0,
    val qty: Int = 0,
    val serviceFee: Int = 0,
    val discount: Int = 0,
    val imageUrl: String = "",
    val locationCity: String = "",
    val locationProvince: String = "",
    val locationFull: String = "",
    val addedNote: String = "",
    var shoppingDate: Date = Calendar.getInstance().time,
    var ExpiredDate: Date = Calendar.getInstance().time,
    var createAt: Date = Calendar.getInstance().time,
    var updateAt: Date = Calendar.getInstance().time
)