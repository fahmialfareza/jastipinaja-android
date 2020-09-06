package com.dinokeylas.jastipinaja

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.USER
import com.dinokeylas.jastipinaja.utils.DialogBuilder
import com.dinokeylas.jastipinaja.utils.ObjectStorageUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.progress_bar
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.BufferedInputStream
import java.io.InputStream


class EditProfileActivity : AppCompatActivity() {

    private var oldProfileImageUrl = "default profile image url"
    private var profileImageUrl = "default profile image url"
    private lateinit var easyImage: EasyImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val mUser = FirebaseAuth.getInstance().currentUser
        var userId = ""
        if (mUser != null) userId = mUser.uid
        getUserData(userId)

        civ_edit_profile_image.setOnClickListener { showImageChooser() }
        btn_update.setOnClickListener { updateProfile(userId) }

        easyImage =
            EasyImage.Builder(this)
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("Jastipin")
                .allowMultiple(true)
                .build()
    }

    private fun getUserData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection(USER).document(userId).get().addOnSuccessListener { document ->
            if (document != null) {
                val user = document.toObject(User::class.java)!!
                fillLayout(user)
            } else {
                Log.d("USER-DATA", "fail to catch user data")
            }
        }.addOnFailureListener {
            Log.d("USER-DATA", it.message)
        }
    }

    private fun showImageChooser() {
        if (isPermissionGranted()) {
            DialogBuilder.ImageChooser(easyImage, this)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                0
            )
        }
    }

    /*method to ask storage access permission */
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun fillLayout(user: User?) {
        et_user_name.setText(user?.userName)
        et_full_name.setText(user?.fullName)
        tv_email_address.text = user?.email
        et_phone_number.setText(user?.phoneNumber)
        et_address.setText(user?.address)
        if (user?.profileImageUrl != "default profile image url") {
            profileImageUrl = user?.profileImageUrl ?: "default profile image url"
            oldProfileImageUrl = user?.profileImageUrl ?: "default profile image url"
            loadDataFromObjectStorage(profileImageUrl)
        }
    }

    private fun updateProfile(userId: String) {
        progress_bar.visibility = View.VISIBLE
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection(USER).document(userId)
        ref.update("userName", et_user_name.text.toString().trim())
        ref.update("fullName", et_full_name.text.toString().trim())
        ref.update("address", et_address.text.toString().trim())
        if (profileImageUrl != "default profile image url") {
            ref.update("profileImageUrl", profileImageUrl)
        }
        ref.update("phoneNumber", et_phone_number.text.toString().trim())
            .addOnSuccessListener {
                progress_bar.visibility = View.GONE
                Toast.makeText(this, "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this, "Pastikan Anda Terkoneksi dengan Internet", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun deleteOldImage(oldProfileImgUrl: String) {
        if (oldProfileImgUrl != "default profile image url") {
            FirebaseStorage.getInstance().getReferenceFromUrl(oldProfileImgUrl).delete()
                .addOnSuccessListener {
                    progress_bar.visibility = View.GONE
                    oldProfileImageUrl = profileImageUrl
                }
        } else {
            progress_bar.visibility = View.GONE
            oldProfileImageUrl = profileImageUrl
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(
            requestCode, resultCode, data, this,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(
                    imageFiles: Array<MediaFile>,
                    source: MediaSource
                ) {
                    Glide.with(applicationContext)
                        .load(imageFiles[0].file)
                        .centerCrop()
                        .into(civ_edit_profile_image)
                    progress_bar.visibility = View.VISIBLE
                    ObjectStorageUtils.uploadImage(imageFiles[0].file, this@EditProfileActivity)
                }

                override fun onImagePickerError(
                    error: Throwable,
                    source: MediaSource
                ) {
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) {}
            })
    }

    public fun onObjectStorageSuccessResponse(imageUrl: String) {
        profileImageUrl = imageUrl
        progress_bar.visibility = View.GONE
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

    private fun setBitmap(bitmap: Bitmap) {
        Glide.with(this)
            .load(bitmap)
            .into(civ_edit_profile_image)
    }
}
