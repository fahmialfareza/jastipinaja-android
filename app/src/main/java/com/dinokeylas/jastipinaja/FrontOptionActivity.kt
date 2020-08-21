package com.dinokeylas.jastipinaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_front_option.*

class FrontOptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_option)

        if(FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this, HomeActivity::class.java))
        }

        btn_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        btn_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
