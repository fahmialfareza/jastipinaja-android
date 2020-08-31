package com.dinokeylas.jastipinaja.contract

import com.dinokeylas.jastipinaja.model.User

interface ProfileContract {
    interface View {
        fun fillDataToLayout(user: User?)
        fun navigateToLogin()
    }

    interface Presenter {
        fun loadUserData()
        fun fillUserDataToLayout(user: User?)
        fun logout()
    }
}