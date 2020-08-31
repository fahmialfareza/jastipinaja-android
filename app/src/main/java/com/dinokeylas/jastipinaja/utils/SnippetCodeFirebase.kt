package com.dinokeylas.jastipinaja.utils

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SnippetCodeFirebase {

    val examplePOJO = ExamplePojo(0, "example")

    fun getDataFromFireStore() {
        FirebaseFirestore.getInstance().collection("collectionName")
            .document("documentId").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //parse data to POJO Example
                    val examplePojo: ExamplePojo? = document.toObject(ExamplePojo::class.java)
                    TODO("do something here if get data is success")
                }
            }.addOnFailureListener {
                TODO("do something here if save data is failed")
            }
    }

    fun saveDataToFirestore() {
        FirebaseFirestore.getInstance().collection("collectionName").add(examplePOJO)
            .addOnSuccessListener {
                TODO("do something here if save data is success")
            }.addOnFailureListener {
                TODO("do something here if save data is fail")
            }
    }

    fun updateDataFirestore() {
        val updatedData = mapOf("name" to "Dino")

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("collectionName").document("documentId")
        ref.update(updatedData)
            .addOnSuccessListener {
                TODO("do something here if update data is success")
            }.addOnFailureListener {
                TODO("do something here if update data is failed")
            }
    }

    fun uploadImageToFirebaseStorage(imageUri: Uri) {

        //this uri get from your chosen image
        val uriProfileImage: Uri = imageUri

        val mStorageRef =
            FirebaseStorage.getInstance().getReference("imageFolder/" + System.currentTimeMillis())
        val uploadTask = mStorageRef.putFile(uriProfileImage)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            mStorageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                //this is imageUrl on FirebaseStorage
                val imageUrl = downloadUri!!.toString()

                TODO("do something here if upload image is success")
            } else {
                TODO("do something here if upload image is failed")
            }
        }
    }

}

data class ExamplePojo(
    val id: Int,
    val name: String
)