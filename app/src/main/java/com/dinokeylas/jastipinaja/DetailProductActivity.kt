package com.dinokeylas.jastipinaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.model.Post
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.POST
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.USER
import com.dinokeylas.jastipinaja.utils.DateUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_product.*

class DetailProductActivity : AppCompatActivity() {
    companion object {
        var POST_ID = "postId"
    }

    private var postId = String()
    private var post = Post()
    private var person = User()
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
        var date = String()
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
        Glide.with(this).load(person.profileImageUrl).into(ivFotoJastiper)
    }

    private fun eventUI() {
//        if(post.postType == 1){
//
//        } else {
//
//        }
        btnBeli.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ProductBuyActivity::class.java
                ).putExtra(ProductBuyActivity.POST_ID, post.postId)
            )
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
}