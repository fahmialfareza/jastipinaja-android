package com.dinokeylas.jastipinaja

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.contract.ProfileContract
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.presenter.ProfilePresenter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment(), ProfileContract.View {

    private lateinit var profilePresenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profilePresenter = ProfilePresenter(this)

        view.btn_logout.setOnClickListener { profilePresenter.logout() }

        view.iv_edit_profile.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        return view
    }

    override fun fillDataToLayout(user: User?) {
        tv_user_name.text = user?.userName
        tv_email.text = user?.email
        tv_phone_number.text = user?.phoneNumber
        tv_location.text = user?.address
        if (user?.profileImageUrl != "default profile image url") {
            Glide.with(this).load(user?.profileImageUrl).into(civ_profile_image)
        }
    }

    override fun navigateToLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
