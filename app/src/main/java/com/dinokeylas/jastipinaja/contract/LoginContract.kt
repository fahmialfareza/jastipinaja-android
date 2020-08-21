package com.dinokeylas.jastipinaja.contract

interface LoginContract {
    interface View {
        fun validateInput(email: String, password: String): Boolean
        fun showToastMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun navigateToHome()
        fun navigateToRegister()
        fun navigateToForgotPassword()
    }

    interface Presenter {
        fun isValidInput(email: String, password: String): Boolean
        fun login(email: String, password: String)
        fun updateUser(password: String)
        fun forgotPassword()
        fun onLoginSuccess()
        fun onLoginFailure()
    }
}