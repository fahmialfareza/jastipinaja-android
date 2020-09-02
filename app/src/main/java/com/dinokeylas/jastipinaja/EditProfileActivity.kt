package com.dinokeylas.jastipinaja

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.model.User
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.*
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
                Toast.makeText(this, "Pastikan Anda Terkoneksi dengan Internet", Toast.LENGTH_SHORT)
                    .show()
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
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uriProfileImage = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriProfileImage)
                civ_profile_image.setImageBitmap(bitmap)
                uploadImage(uriProfileImage)
//                uploadToObjectStorage(uriProfileImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun uploadToObjectStorage(imageUri: Uri?) {
        val file = imageUri?.toFile()
        val fileName = getFileName(imageUri!!)

        AsyncTask.execute {
            val client = OkHttpClient()

            val body: RequestBody = MultipartBuilder()
                .addFormDataPart(
                    "file", fileName,
                    RequestBody.create(MediaType.parse("application/octet-stream"), file)
                )
                .build()
            val request: Request = Request.Builder()
                .url("https://api.thebigbox.id/object-storage/0.0.2/stores/jastipinaja")
                .method("POST", body)
                .addHeader("x-api-key", "3lr7BsqF0N6RqWTffs9D6d3d7JqiGekT")
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("accept", "application/json")
                .build()

            val response: Response = client.newCall(request).execute()

            if (response.isSuccessful) {
                Log.d("File upload","success, file name: $fileName")
                Toast.makeText(this, "Upload Gambar Berhasil", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("File upload", "failed")
                Toast.makeText(this, "Upload Gambar Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

//    private fun getRealPathFromURI(contentURI: Uri): String? {
//        val result: String
//        val cursor =
//            contentResolver.query(contentURI, null, null, null, null)
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.path
//        } else {
//            cursor.moveToFirst()
//            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//            result = cursor.getString(idx)
//            cursor.close()
//        }
//        return result
//    }
//
//
//    private fun uriToFilename(uri: Uri): String? {
//        var path: String? = null
//        path = if (Build.VERSION.SDK_INT < 19 && Build.VERSION.SDK_INT > 11) {
//            getRealPathFromURI_API11to18(this, uri)
//        } else {
//            getFilePath(this, uri)
//        }
//        return path
//    }
//
//    fun getRealPathFromURI_API11to18(
//        context: Context?,
//        contentUri: Uri?
//    ): String? {
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        var result: String? = null
//        val cursorLoader = CursorLoader(
//            context!!,
//            contentUri!!, proj, null, null, null
//        )
//        val cursor: Cursor = cursorLoader.loadInBackground()!!
//        if (cursor != null) {
//            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor.moveToFirst()
//            result = cursor.getString(column_index)
//        }
//        return result
//    }
//
//    fun getFilePath(context: Context, uri: Uri): String? {
//        //Log.e("uri", uri.getPath());
//        var filePath: String? = ""
//        if (DocumentsContract.isDocumentUri(context, uri)) {
//            val wholeID: String = DocumentsContract.getDocumentId(uri)
//            //Log.e("wholeID", wholeID);
//            // Split at colon, use second item in the array
//            val splits = wholeID.split(":").toTypedArray()
//            if (splits.size == 2) {
//                val id = splits[1]
//                val column =
//                    arrayOf(MediaStore.Images.Media.DATA)
//                // where id is equal to
//                val sel = MediaStore.Images.Media._ID + "=?"
//                val cursor: Cursor = context.contentResolver.query(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    column, sel, arrayOf(id), null
//                )
//                val columnIndex = cursor.getColumnIndex(column[0])
//                if (cursor.moveToFirst()) {
//                    filePath = cursor.getString(columnIndex)
//                }
//                cursor.close()
//            }
//        } else {
//            filePath = uri.path
//        }
//        return filePath
//    }


//    Log.e("ERRRRROOOOORR", file?.path)
//    Log.e("ERRRRROOOOORR", fileName)
//    Log.e("ERRRRROOOOORR-Uri", imageUri.toString())
//    Log.e("ERRRRROOOOORR-path", imageUri.path)
}
