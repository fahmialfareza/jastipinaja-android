package com.dinokeylas.jastipinaja

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {

    private val CHOOSE_IMAGE = 101
    private var oldProfileImageUrl = "default profile image url"
    private var profileImageUrl = "default profile image url"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val mUser = FirebaseAuth.getInstance().currentUser
        var userId = ""
        if (mUser != null) userId = mUser.uid
        getUserData(userId)

        civ_profile_image.setOnClickListener { showImageChooser() }
        btn_update.setOnClickListener { updateProfile(userId) }
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
            TODO("if something goes wrong, do something here")
        }
    }

    private fun showImageChooser() {
        if (isPermissionGranted()) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                Intent.createChooser(galleryIntent, "Pilih foto profil Anda"),
                CHOOSE_IMAGE
            )
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
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
            Glide.with(this)
                .load(user?.profileImageUrl)
                .into(civ_profile_image)
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
                Toast.makeText(this, "Pastikan Anda Terkoneksi dengan Internet", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImage(uriProfileImage: Uri?) {
        progress_bar.visibility = View.VISIBLE
        val mStorageRef = FirebaseStorage.getInstance()
            .getReference("profileImage/" + System.currentTimeMillis() + ".jpg")

        val uploadTask = mStorageRef.putFile(uriProfileImage!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            mStorageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                profileImageUrl = downloadUri!!.toString()
                deleteOldImage(oldProfileImageUrl)
            } else {
                progress_bar.visibility = View.GONE
                Toast.makeText(this, "Gagal Update Profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteOldImage(oldProfileImgUrl: String){
        if(oldProfileImgUrl != "default profile image url"){
            FirebaseStorage.getInstance().getReferenceFromUrl(oldProfileImgUrl).delete().addOnSuccessListener {
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
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uriProfileImage = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriProfileImage)
                civ_profile_image.setImageBitmap(bitmap)
                uploadImage(uriProfileImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

//    fun uploadImageToBigBoxAPI(){
//        AsyncTask.execute {
//            val client = OkHttpClient()
//
//            val body: RequestBody = FormEncodingBuilder()
//                .add("subject", "Selamat Datang di JastipinAja")
//                .add(
//                    "message",
//                    "Halo " + username +"!\n" +
//                            "Terima kasih telah mendaftar dan menjadi bagian dari JastipinAja\n" +
//                            "Selamat datang di JastipinAja, aplikasi titip barang apapun dimana aja!\n\n" +
//                            "Salam,\n" +
//                            "Tim JastipinAja :)"
//                )
//                .add("recipient", email)
//                .build()
//
//            val request: Request = Request.Builder()
//                .url("https://api.thebigbox.id/mail-sender/0.0.1/mails")
//                .method("POST", body)
//                .addHeader("x-api-key", "3lr7BsqF0N6RqWTffs9D6d3d7JqiGekT")
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .build()
//
//            client.newCall(request).execute()
//        }
//    }
//
//    fun a(){
//        val client: OkHttpClient = OkHttpClient()
////        var mediaType: MediaType = MediaType.parse("text/plain")
//
//        var body: RequestBody = RequestBody.create()
//
////            .add("file", "/C:/Users/Fahmi Alfareza/Downloads/Dino_Keylas.jpg",  // Ini file yang diupload
////                RequestBody.create(MediaType.parse("application/octet-stream"),
////                    File("/C:/Users/Fahmi Alfareza/Downloads/Dino_Keylas.jpg")
////                )) // Ini file yang diupload
////            .build()
//        var request: Request = Builder()
//            .url("https://api.thebigbox.id/object-storage/0.0.2/stores/jastipinaja") // Bucket jastipinaja udah ada tinggal dipake aja
//            .method("POST", body)
//            .addHeader("x-api-key", "3lr7BsqF0N6RqWTffs9D6d3d7JqiGekT")
//            .build()
//        var response: Response<*> = client.newCall(request).execute()
//    }

}
