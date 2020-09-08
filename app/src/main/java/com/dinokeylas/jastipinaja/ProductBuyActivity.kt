package com.dinokeylas.jastipinaja

import android.app.AlertDialog
import android.content.Intent
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
import com.dinokeylas.jastipinaja.model.Transaction
import com.dinokeylas.jastipinaja.utils.Constant
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.TRANSACTION
import com.dinokeylas.jastipinaja.utils.DateUtils
import com.google.firebase.auth.FirebaseAuth
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
import java.util.*

class ProductBuyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    companion object {
        var POST_ID = "postId"
    }

    private var postId = ""
    private var userId = ""
    private var post = Post()
    private var qty: Int = 0
    private var expedition = String()
    private var deliveryFee = 0
    private var totalPay = 0
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
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        postId = intent.getStringExtra(POST_ID)
        initPostData()
    }

    private fun initUI() {
        var date = String()
        totalPay = deliveryFee + post.product.price + post.product.serviceFee
        if (post.postType == 1) {
            date = DateUtils.getStringFormatedDate(post.product.shoppingDate)
        } else {
            date = DateUtils.getStringFormatedDate(post.product.ExpiredDate)
        }

        tvNamaBarang.text = post.product.name
        tvDateBarang.text = date
        tvDeskripsiBarang.text = post.product.description
        tvTempatBeliBarang.text = post.product.locationFull
        etHargaBarang_.text = post.product.price.toString()
        etJasaJastip_.text = post.product.serviceFee.toString()
        etBiayaTotal.text = totalPay.toString()
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
            if (etAlamatKirim?.text.toString().isEmpty()){
                etAlamatKirim?.error = "Harus diisi"
                return@setOnClickListener
            }
            showInformationDialog()
        }
    }

    //get data from Firebase
    private fun initPostData() {
        FirebaseFirestore.getInstance().collection(Constant.Collections.POST).document(postId).get()
            .addOnSuccessListener {
                if (it != null) {
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
                etBiayaKirim_.text = "12000"
                deliveryFee = 12000
            }
            "JNE" -> {
                etBiayaKirim_.text = "24000"
                deliveryFee = 24000
            }
            else -> {
                etBiayaKirim_.text = "15000"
                deliveryFee = 15000
            }
        }
    }

    private fun showInformationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Perhation")
        builder.setMessage("Pastikan data yang Anda isikan telah benar")
        builder.setPositiveButton("Oke") { _, _ ->
            saveDataToFirebase(createTransaction())
        }
        builder.setNegativeButton("Batal") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun createTransaction(): Transaction {
        val jastiper: String
        val purchaser: String
        if (post.postType == 1) {
            jastiper = post.author
            purchaser = userId
        } else {
            jastiper = userId
            purchaser = post.author
        }

        return Transaction(
            "",
            post.postType,
            1,
            post,
            qty,
            jastiper,
            purchaser,
            etHargaBarang_.text.toString().toInt(),
            etJasaJastip_.text.toString().toInt(),
            deliveryFee,
            etBiayaTotal.text.toString().toInt(),
            "",
            expedition,
            etAlamatKirim.text.toString(),
            false,
            Constant.TransactionStatus.ON_PROGRESS,
            Constant.TransactionProgress.ORDER,
            etKeteranganBarang_.text.toString(),
            Calendar.getInstance().time,
            Calendar.getInstance().time,
            Calendar.getInstance().time
        )
    }

    private fun saveDataToFirebase(transaction: Transaction) {
        pb_product_buy.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection(TRANSACTION).add(transaction)
            .addOnSuccessListener {
                pb_product_buy.visibility = View.GONE
                startActivity(
                    Intent(this, PaymentActivity::class.java)
                        .putExtra(PaymentActivity.ARG_TOTAL_TAGIHAN, totalPay)
                        .putExtra(PaymentActivity.ARG_ALAMAT_KIRIM, etAlamatKirim.text.toString())
                )
            }.addOnFailureListener {
                pb_product_buy.visibility = View.GONE
            }
    }
}
