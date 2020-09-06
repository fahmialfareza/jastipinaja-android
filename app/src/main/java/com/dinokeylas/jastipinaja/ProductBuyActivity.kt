package com.dinokeylas.jastipinaja

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.model.Post
import com.dinokeylas.jastipinaja.utils.Constant
import com.dinokeylas.jastipinaja.utils.DateUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_detail_product.tvDateBarang
import kotlinx.android.synthetic.main.activity_detail_product.tvDeskripsiBarang
import kotlinx.android.synthetic.main.activity_detail_product.tvNamaBarang
import kotlinx.android.synthetic.main.activity_detail_product.tvTempatBeliBarang
import kotlinx.android.synthetic.main.activity_posting.*
import kotlinx.android.synthetic.main.activity_posting.minusLyt
import kotlinx.android.synthetic.main.activity_posting.plusLyt
import kotlinx.android.synthetic.main.activity_posting.tvQty
import kotlinx.android.synthetic.main.activity_product_buy.*

class ProductBuyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    companion object {
        var POST_ID = "postId"
    }

    private var postId = ""
    private var post = Post()
    private var qty: Int = 0
    private var expedition = String()
    private var deliveryFee = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_buy)
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
        val totalPay = deliveryFee + post.product.price + post.product.serviceFee
        if (post.postType == 1) {
            date = DateUtils.getStringFormatedDate(post.product.shoppingDate)
        } else {
            date = DateUtils.getStringFormatedDate(post.product.ExpiredDate)
        }

        tvNamaBarang.text = post.product.name
        tvDateBarang.text = date
        tvDeskripsiBarang.text = post.product.description
        tvTempatBeliBarang.text = post.product.locationFull
        etHargaBarang_.text = "Rp " + post.product.price.toString()
        etJasaJastip_.text = "Rp " + post.product.serviceFee.toString()
        etBiayaTotal.text = "Rp " + totalPay
        Glide.with(this).load(post.product.imageUrl).into(ivBarang_)

        val spinnerEkspedisi = findViewById<Spinner>(R.id.spinnerEkspedisi)
        val arrayAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.expedition,
            android.R.layout.simple_spinner_item
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEkspedisi.adapter = arrayAdapter
        spinnerEkspedisi.onItemSelectedListener = this
    }

    private fun eventUI() {

        plusLyt.setOnClickListener {
            ++qty
            tvQty.text = qty.toString().trim()
        }

        minusLyt.setOnClickListener {
            if (qty == 0) {
                Toast.makeText(this, "Tidak boleh kurang dari nol", Toast.LENGTH_SHORT).show()
                Log.d("QTY", qty.toString())
            } else {
                --qty
                tvQty.text = qty.toString().trim()
            }
        }

        btn_to_payment?.setOnClickListener {
            showInformationDialog()
        }
    }

    //get data from Firebase
    private fun initPostData() {
        FirebaseFirestore.getInstance().collection(Constant.Collections.POST).document(postId).get()
            .addOnSuccessListener {
                if(it!=null){
                    post = it.toObject(Post::class.java)!!
                    post.postId = it.id
                    initUI()
                } else {
                    Log.d("DETAIL-POST", "No Post data")
                }
            }.addOnFailureListener {
                Log.e("DETAIL-POST", it.message)
            }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO()
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
        expedition = adapterView?.getItemAtPosition(position).toString()
        when (expedition) {
            "Go-Send" -> {
                etBiayaKirim_.text = "Rp 12000"
                deliveryFee = 12000
            }
            "JNE" -> {
                etBiayaKirim_.text = "Rp 24000"
                deliveryFee = 24000
            }
            else -> {
                etBiayaKirim_.text = "Rp 15000"
                deliveryFee = 15000
            }
        }
    }

    private fun showInformationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Perhation")
        builder.setMessage("Pastikan data yang Anda isikan telah benar")
        builder.setPositiveButton("Oke") { _, _ -> }
        builder.setNegativeButton("Batal") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
