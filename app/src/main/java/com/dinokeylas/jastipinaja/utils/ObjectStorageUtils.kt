package com.dinokeylas.jastipinaja.utils

import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dinokeylas.jastipinaja.EditProfileActivity
import com.dinokeylas.jastipinaja.model.ObjectStorageResponse
import com.google.gson.Gson
import com.squareup.okhttp.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File

class ObjectStorageUtils {
    companion object{
        @JvmStatic
        fun uploadImage(file: File, activity: EditProfileActivity){
            var imageUrl = "https://api.thebigbox.id/object-storage/0.0.2/stores/jastipinaja"
            val fileName = file.name
            AsyncTask.execute {
                val client = OkHttpClient()

                val body: RequestBody = MultipartBuilder().type(MultipartBuilder.FORM)
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

                try {
                    val response = client.newCall(request).execute()
                    val jsonResponseString = response.body().string()
                    try {
                        val apiResponse = Gson().fromJson(jsonResponseString, ObjectStorageResponse::class.java)
                        imageUrl = imageUrl + "/" + apiResponse.data.name
                        activity.onObjectStorageSuccessResponse(imageUrl)
                    } catch (e: java.lang.Exception){
                        Log.d("FAILURE CONVERT", e.message)
                    }
                } catch (e: Exception) {
                    Log.d("ERROR-UPLOAD-IMAGE", e.message)
                    Toast.makeText(activity, "Gambar gagal diunggah, coba ulangi kembali", Toast.LENGTH_SHORT).show()
                    activity.progress_bar.visibility = View.GONE
                }
            }
        }
    }
}