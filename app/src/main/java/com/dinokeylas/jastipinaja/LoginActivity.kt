package com.dinokeylas.jastipinaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.dinokeylas.jastipinaja.contract.LoginContract
import com.dinokeylas.jastipinaja.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginPresenter = LoginPresenter(this)

        btn_login.setOnClickListener {
            loginPresenter.login(et_email.text.toString(), et_password.text.toString())
        }

        tv_forget_password.setOnClickListener {
            loginPresenter.forgotPassword()
        }

        iv_back.setOnClickListener {
            startActivity(Intent(this, FrontOptionActivity::class.java))
        }
    }

    override fun validateInput(email: String, password: String): Boolean {

        if (email.isEmpty()) {
            et_email.error = "Email tidak boleh kosong"
            et_email.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.error = "Pastikan format penulisan email benar"
            et_email.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            et_password.error = "Kata sandi tidak boleh kosong"
            et_password.requestFocus()
            return false
        }

        return true
    }

    override fun showToastMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    override fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun navigateToForgotPassword() {
        startActivity(Intent(this, FrontOptionActivity::class.java))
    }
}
