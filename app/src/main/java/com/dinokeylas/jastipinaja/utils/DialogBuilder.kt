package com.dinokeylas.jastipinaja.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import pl.aprilapps.easyphotopicker.EasyImage

object DialogBuilder {
    fun ImageChooser(easyImage: EasyImage, context: Activity) {
        val item =
            arrayOf<CharSequence>("Kamera", "Galeri")
        val request = AlertDialog.Builder(context)
            .setTitle("Choose Image")
            .setItems(item) { dialogInterface: DialogInterface, i: Int ->
                when (i) {
                    0 -> {
                        easyImage.openCameraForImage(context)
                    }
                    1 -> {
                        easyImage.openGallery(context)
                    }
                }
            }
        request.create()
        request.show()
    }
}