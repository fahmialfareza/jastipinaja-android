package com.dinokeylas.jastipinaja

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.model.Post
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.POST
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.USER
import com.dinokeylas.jastipinaja.utils.DateUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.activity_detail_product.*
import java.io.BufferedInputStream
import java.io.InputStream

class DetailProductActivity : AppCompatActivity() {
    companion object {
        var POST_ID = "postId"
    }

    private var postId = String()
    private var post = Post()
    private var person = User()
    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
    }

    private fun initObject() {
        postId = intent.getStringExtra(POST_ID)
        initPostData()
    }

    private fun initUI() {
        val date: String
        if (post.postType == 1) {
            date = DateUtils.getStringFormatedDate(post.product.shoppingDate)
            tvHargaJastipBarang.visibility = View.VISIBLE
            btnBeli.setText(R.string.label_beli_sekarang)
        } else {
            date = DateUtils.getStringFormatedDate(post.product.ExpiredDate)
            tvHargaJastipBarang.visibility = View.GONE
            btnBeli.setText(R.string.label_bantu_belikan)
        }

        tvNamaBarang.text = post.product.name
        tvDateBarang.text = date
        tvDeskripsiBarang.text = post.product.description
        tvTempatBeliBarang.text = post.product.locationFull
        tvHargaBeliBarang.text = "Harga Barang: Rp" + post.product.price.toString()
        tvHargaJastipBarang.text = "Jasa Titip: Rp" + post.product.serviceFee.toString()
        tvPerson.text = person.fullName

        Glide.with(this).load(post.product.imageUrl).into(ivBarang)
        Glide.with(this).load(R.drawable.person_icon).into(ivFotoJastiper)
//        if (person.profileImageUrl != "default profile image url") {
//            loadDataFromObjectStorage(person.profileImageUrl)
//        }
    }

    private fun eventUI() {
//        if(post.postType == 1){
//
//        } else {
//
//        }
        btnBeli.setOnClickListener {
            ConfirmationDialog.Builder(this)
                .setTitle("Pilihan Transaksi")
                .setDescription("Silahkan pilih bagaimana anda mengantarkan barang ini?")
                .setOkText("Pengiriman")
                .setCancelText("COD")
                .setListener(object : ConfirmationDialog.ConfirmationDialogListener{
                    override fun setOnOkListener() {
                        startActivity(
                            Intent(
                                applicationContext,
                                ProductBuyActivity::class.java
                            ).putExtra(ProductBuyActivity.POST_ID, post.postId)
                        )
                    }

                    override fun setOnCancelListener() {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${person.phoneNumber}&text=&source=&data=&app_absent=")))
                    }

                })
                .build()
                .show()
        }
    }

    //get data from Firebase
    private fun initPostData() {
        FirebaseFirestore.getInstance().collection(POST).document(postId).get()
            .addOnSuccessListener {
                if (it != null) {
                    post = it.toObject(Post::class.java)!!
                    post.postId = it.id
                    initPersonData()
                } else {
                    Log.d("DETAIL-POST", "No Post data")
                }
            }.addOnFailureListener {
                Log.e("DETAIL-POST", it.message)
            }
    }

    //get data from Firebase
    private fun initPersonData() {
        FirebaseFirestore.getInstance().collection(USER).document(post.author).get()
            .addOnSuccessListener {
                if (it != null) {
                    person = it.toObject(User::class.java)!!
                    initUI()
                } else {
                    Log.d("DETAIL-POST", "No User data")
                }
            }.addOnFailureListener {
                Log.e("DETAIL-POST", it.message)
            }
    }

    private fun loadDataFromObjectStorage(imageUrl: String?) {
        AsyncTask.execute {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(imageUrl)
                .get()
                .addHeader("x-api-key", "3lr7BsqF0N6RqWTffs9D6d3d7JqiGekT")
                .addHeader("accept", "application/json")
                .build()

            try {
                val response = client.newCall(request).execute()
                try {
                    val inputStream: InputStream = response.body().byteStream()
                    Log.i("INPUT-STREAM", "Input stream value = $inputStream")
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    val bit = BitmapFactory.decodeStream(bufferedInputStream)
                    Log.i("BITMAP", "bitmap value = $bit")
//                    setBitmap(bit)
                } catch (e: java.lang.Exception) {
                    Log.d("FAILURE CONVERT", e.message)
                }
            } catch (e: Exception) {
                Log.d("ERROR-DOWNLOAD-IMAGE", e.message)
            }
        }
    }

    private fun setBitmap(bit: Bitmap) {
        ivFotoJastiper.setImageBitmap(bit)
    }
}