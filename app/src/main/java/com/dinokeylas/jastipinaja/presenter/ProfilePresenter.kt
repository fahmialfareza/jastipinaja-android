package com.dinokeylas.jastipinaja.presenter

import android.util.Log
import com.dinokeylas.jastipinaja.contract.ProfileContract
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfilePresenter(_view: ProfileContract.View): ProfileContract.Presenter {

    private val view: ProfileContract.View = _view

    init { loadUserData() }

    override fun loadUserData() {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val mUser = FirebaseAuth.getInstance().currentUser
        val userId = mUser?.uid

        firebaseFirestore.collection(USER).document(userId ?: "user").get()
            .addOnSuccessListener {document ->
                if(document!=null){
                    val user: User? = document.toObject(User::class.java)
                    fillUserDataToLayout(user)
                } else {
                    Log.d("USER-DATA", "user data is null")
                }
            }.addOnFailureListener {
                Log.d("USER-DATA", it.message)
            }
    }

    override fun fillUserDataToLayout(user: User?) { view.fillDataToLayout(user) }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
        view.navigateToLogin()
    }
}