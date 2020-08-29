package com.dinokeylas.jastipinaja.utils

import android.os.AsyncTask
import com.squareup.okhttp.*
import java.lang.Exception


class EmailUtils {
    companion object {
        @JvmStatic
        @Throws(Exception::class)
        fun sendRegisterConfirmationEmail(email: String, username: String) {
            AsyncTask.execute {
                val client = OkHttpClient()

                val body: RequestBody = FormEncodingBuilder()
                    .add("subject", "Selamat Datang di JastipinAja")
                    .add(
                        "message",
                        "Halo " + username +"!\n" +
                                "Terima kasih telah mendaftar dan menjadi bagian dari JastipinAja\n" +
                                "Selamat datang di JastipinAja, aplikasi titip barang apapun dimana aja!\n\n" +
                                "Salam,\n" +
                                "Tim JastipinAja :)"
                    )
                    .add("recipient", email)
                    .build()

                val request: Request = Request.Builder()
                    .url("https://api.thebigbox.id/mail-sender/0.0.1/mails")
                    .method("POST", body)
                    .addHeader("x-api-key", "3lr7BsqF0N6RqWTffs9D6d3d7JqiGekT")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()

                client.newCall(request).execute()
            }
        }
    }
}