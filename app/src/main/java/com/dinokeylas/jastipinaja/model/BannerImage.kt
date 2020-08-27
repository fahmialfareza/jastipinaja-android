package com.dinokeylas.jastipinaja.model

import com.google.firebase.firestore.Exclude
import java.util.*

data class BannerImage (
    @get:Exclude var bannerId: String = "",
    val name: String = "",
    val url: String = "",
    val createAt: Date = Calendar.getInstance().time
)