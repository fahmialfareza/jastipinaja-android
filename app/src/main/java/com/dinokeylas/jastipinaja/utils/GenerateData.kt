package com.dinokeylas.jastipinaja.utils

import com.google.firebase.firestore.Exclude
import java.util.*
import kotlin.collections.ArrayList

class GenerateData {
    companion object {
        @JvmStatic
        fun generateTransactionList(): ArrayList<TransactionExample> {
            var tranList = ArrayList<TransactionExample>()
            var t1 = TransactionExample(
                "0",
                "0",
                "dasd",
                "dinok@gmail.com",
                Calendar.getInstance().time,
                "Baju Baru",
                120000,
                "httpImage",
                "Malang",
                1,
                120000,
                "Arif",
                false,
                "onProgress"
            )
            var t2 = TransactionExample(
                "0",
                "0",
                "dasd",
                "dinok@gmail.com",
                Calendar.getInstance().time,
                "Baju Baru 2",
                120000,
                "httpImage",
                "Malang",
                1,
                120000,
                "Arif",
                false,
                "onProgress"
            )
            tranList.add(t1)
            tranList.add(t2)
            return tranList
        }


        @JvmStatic
        fun generateMessageList(): ArrayList<MessageExample>{
            var messageList = ArrayList<MessageExample>()

            val m1 = MessageExample("a","https://firebasestorage.googleapis.com/v0/b/jastipin-aja.appspot.com/o/Cities%2Fbogor.jpg?alt=media&token=0c35985c-2329-465e-9540-f657bddcf6e6",
                "dino", "this is message", Calendar.getInstance().time, 0)
            val m2 = MessageExample("a", "https://firebasestorage.googleapis.com/v0/b/jastipin-aja.appspot.com/o/Cities%2Fbogor.jpg?alt=media&token=0c35985c-2329-465e-9540-f657bddcf6e6",
                "doni", "this is message 2", Calendar.getInstance().time, 2)
            messageList.add(m1)
            messageList.add(m2)
            return messageList
        }

    }
}

data class TransactionExample(
    @get:Exclude var transactionId: String = "",
    val transactionCode: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val date: Date = Calendar.getInstance().time,
    val itemName: String = "",
    val itemPrise: Int = 0,
    val imageUrl: String = "",
    val userLocation: String = "",
    val itemQty: Int = 0,
    val totalPay: Int = 0,
    val sellerName: String = "",
    val done: Boolean = false,
    val transactionProgress: String = ""
)

data class MessageExample(
    @get:Exclude var messageId: String = "",
    val imageUrl: String = "",
    val receiver: String = "",
    val preview: String = "",
    val date: Date = Calendar.getInstance().time,
    val count: Int = 0
)