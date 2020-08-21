package com.dinokeylas.jastipinaja.presenter

import com.dinokeylas.jastipinaja.contract.LoginContract
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.USER
import com.dinokeylas.jastipinaja.utils.MD5
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPresenter(_view: LoginContract.View): LoginContract.Presenter {

    private val view: LoginContract.View = _view

    override fun login(email: String, password: String) {
        val mAuth = FirebaseAuth.getInstance()

        if (isValidInput(email, password)){
            view.showProgressBar()
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    updateUser(password)
                }
                .addOnFailureListener {
                    onLoginFailure()
                }
        }
    }

    override fun isValidInput(email: String, password: String): Boolean {
        return view.validateInput(email, password)
    }

    override fun onLoginSuccess() {
        view.hideProgressBar()
        view.showToastMessage("Anda Berhasil Login")
        view.navigateToHome()
    }

    override fun updateUser(password: String) {
        val user = FirebaseAuth.getInstance().currentUser
        FirebaseFirestore.getInstance().collection(USER).document(user?.uid!!)
            .update("password", MD5.encript(password)).addOnSuccessListener {
                onLoginSuccess()
            }.addOnFailureListener{
                onLoginFailure()
            }
    }

    override fun onLoginFailure() {
        view.hideProgressBar()
        view.showToastMessage("Pastikan pasangan email dan password yang anda masukkan adalah benar")
    }

    override fun forgotPassword(){
        view.navigateToForgotPassword()
    }
}