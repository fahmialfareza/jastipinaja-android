package com.dinokeylas.jastipinaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.dinokeylas.jastipinaja.contract.RegisterContract
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_email
import kotlinx.android.synthetic.main.activity_register.et_password
import kotlinx.android.synthetic.main.activity_register.iv_back
import kotlinx.android.synthetic.main.activity_register.progress_bar

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var registerPresenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerPresenter = RegisterPresenter(this)

        btn_register.setOnClickListener{
            val user = User(
                et_full_name.text.toString(),
                et_full_name.text.toString(),
                et_email.text.toString(),
                "defaul address",
                et_phone_number.text.toString(),
                "default profile image url",
                et_password.text.toString()
            )
            registerPresenter.register(user)
        }

        iv_back.setOnClickListener {
            startActivity(Intent(this, FrontOptionActivity::class.java))
        }

    }

    override fun validateInput(user: User): Boolean {

        //full name should not empty
        if (user.fullName.isEmpty()){
            et_full_name.error = "Nama Tidak Boleh Kosong"
            et_full_name.requestFocus()
            return false
        }

        //email should not empty
        if (user.email.isEmpty()){
            et_email.error = "Email Tidak Boleh Kosong"
            et_email.requestFocus()
            return false
        }

        //email should follow pattern
        if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()){
            et_email.error = "Format email salah"
            et_email.requestFocus()
            return false
        }

        //phone number should not empty
        if (user.phoneNumber.isEmpty()){
            et_phone_number.error = "Nomor Telepon Tidak Boleh Kosong"
            et_phone_number.requestFocus()
            return false
        }

        //password should not empty
        if (user.password.isEmpty()){
            et_password.error = "Kata Sandi Tidak Boleh Kosong"
            et_password.requestFocus()
            return false
        }

        //password contains minimum 6 character
        if (user.password.length<6){
            et_password.error = "Kata Sandi minimal terdiri dari 6 karakter"
            et_password.requestFocus()
            return false
        }

        //password should not null
        if (et_password_validation.text.toString().isEmpty()){
            et_password_validation.error = "Kata Sandi Tidak Boleh Kosong"
            et_password_validation.requestFocus()
            return false
        }

        // password and password validation must be same
        if (user.password != et_password_validation.text.toString()){
            et_password.error = "Kata Sandi Harus Sama"
            et_password.requestFocus()
            return false
        }

        return true
    }

    override fun showToastMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.show()
    }

    override fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    override fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

}