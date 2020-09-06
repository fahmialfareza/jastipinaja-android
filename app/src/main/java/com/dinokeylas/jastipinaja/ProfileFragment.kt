package com.dinokeylas.jastipinaja

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dinokeylas.jastipinaja.contract.ProfileContract
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.presenter.ProfilePresenter
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.BufferedInputStream
import java.io.InputStream


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
            loadDataFromObjectStorage(user?.profileImageUrl)
        }
    }

    override fun navigateToLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun loadDataFromObjectStorage(imageUrl: String?) {
        AsyncTask.execute {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(imageUrl)
                .get()
                .addHeader("x-api-key", "3lr7BsqF0N6RqWTffs9D6d3d7JqiGekT")
                .addHeader("accept", "application/json")
                .build()

            try {
                val response = client.newCall(request).execute()
                try {
                    val inputStream: InputStream = response.body().byteStream()
                    Log.i("INPUT-STREAM", "Input stream value = $inputStream")
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    val bitmap = BitmapFactory.decodeStream(bufferedInputStream)
                    Log.i("BITMAP", "bitmap value = $bitmap")
                    setBitmap(bitmap)
                } catch (e: java.lang.Exception) {
                    Log.d("FAILURE CONVERT", e.message)
                }
            } catch (e: Exception) {
                Log.d("ERROR-DOWNLOAD-IMAGE", e.message)
            }
        }
    }

    private fun setBitmap(bit: Bitmap) {
        civ_profile_image.setImageBitmap(bit)
    }
}
