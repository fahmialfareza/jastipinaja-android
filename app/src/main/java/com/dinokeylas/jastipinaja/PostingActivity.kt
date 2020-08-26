package com.dinokeylas.jastipinaja

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_posting.*

class PostingActivity : AppCompatActivity() {
    companion object {
        const val FLAG = "FLAG"
    }

    /*flag = 1 - Post Jastip
      flag = 2 - Post Request Barang*/
    private var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
    }

    private fun initObject() {
        flag = intent.getIntExtra(FLAG, 0)
    }

    private fun initUI() {
        //INIT TOOLBAR
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (flag == 1) {
            supportActionBar?.title = getString(R.string.page_post_jastip)
        } else {
            supportActionBar?.title = getString(R.string.page_post_request_barang)
        }
    }

    private fun eventUI() {

    }
}