package com.dinokeylas.jastipinaja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {
    companion object {
        const val ARG_TOTAL_TAGIHAN = "ARG_TOTAL_TAGIHAN"
        const val ARG_ALAMAT_KIRIM = "ARG_ALAMAT_KIRIM"
    }

    private var totalTagihan = 0
    private var alamatKirim = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        init()
    }

    private fun init() {
        initObject()
        initUI()
    }

    private fun initObject() {
        totalTagihan = intent.getIntExtra(ARG_TOTAL_TAGIHAN, 0)
        alamatKirim = intent.getStringExtra(ARG_ALAMAT_KIRIM)
    }

    private fun initUI() {
        tvAlamatKirim?.text = alamatKirim
        tvTotalTagihan?.text = "Total Tagihan ${totalTagihan}"
    }
}