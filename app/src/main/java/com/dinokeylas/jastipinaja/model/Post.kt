package com.dinokeylas.jastipinaja.model

import com.google.firebase.firestore.Exclude
import java.util.*

data class Post (
    @get:Exclude var postId: String = "",
    val product: Product = Product(),
    val title: String = "",
    val author: User = User(),
    val postType: Int = 0,
    val isLive: Boolean = false,
    val createAt: Date = Calendar.getInstance().time,
    val updateAt: Date = Calendar.getInstance().time,
    val expiredAt: Date = Calendar.getInstance().time
)