package com.dinokeylas.jastipinaja.model

import com.google.firebase.firestore.Exclude
import java.util.*

data class Transaction (
    @get:Exclude val tranId: String = "",
    val transactionType: Int = 0, // 1: beli produk, 2: bantu belikan
    val transactionMethod: Int = 0, // 1: in app, 2: cod
    val post: Post = Post(),
    val qty: Int = 0, // banyak barang
    val jastiper: String = "", // id jastiper
    val purchaser: String = "", // id pembeli
    val productPrice: Int = 0, // total harga produk
    val serviceFee: Int = 0, // total jasa titip
    val deliveryFee: Int = 0, // biaya kirim
    val totalPay: Int = 0, // total bayar
    val paymentVendor: String = "", // verndor pembayaran
    val expeditionVendor: String = "", // vendor pengiriman paket
    val purchaserAddress: String = "", // alamat pembeli
    var isDOne: Boolean = false, // sudah selesai apa blm
    var status: String = "", // berhasil atau dibatalkan
    var progress: String = "", // tahapan
    var note: String = "", // catata khusus (optional)
    var createAt: Date = Calendar.getInstance().time,
    var updateAt: Date = Calendar.getInstance().time,
    var doneAt: Date = Calendar.getInstance().time
)