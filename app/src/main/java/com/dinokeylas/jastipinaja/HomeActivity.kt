package com.dinokeylas.jastipinaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(this, FrontOptionActivity::class.java))
        }
    }
}
