package com.dinokeylas.jastipinaja

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dinokeylas.jastipinaja.utils.DialogBuilder
import kotlinx.android.synthetic.main.activity_posting.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource


class PostingActivity : AppCompatActivity() {
    companion object {
        const val FLAG = "FLAG"
    }

    /*flag = 1 - Post Jastip
      flag = 2 - Post Request Barang*/
    private var flag = 0
    private lateinit var easyImage: EasyImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
    }

    private fun initObject() {
        flag = intent.getIntExtra(FLAG, 0)
        easyImage =
            EasyImage.Builder(this) // Chooser only
                // Will appear as a system chooser title, DEFAULT empty string
                //.setChooserTitle("Pick media")
                // Will tell chooser that it should show documents or gallery apps
                //.setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)  you can use this or the one below
                //.setChooserType(ChooserType.CAMERA_AND_GALLERY)
                // Setting to true will cause taken pictures to show up in the device gallery, DEFAULT false
                .setCopyImagesToPublicGalleryFolder(false) // Sets the name for images stored if setCopyImagesToPublicGalleryFolder = true
                .setFolderName("Jastipin") // Allow multiple picking
                .allowMultiple(true)
                .build()

    }

    private fun initUI() {
        //INIT TOOLBAR
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (flag == 1) {
            supportActionBar?.title = getString(R.string.page_post_jastip)
        } else {
            supportActionBar?.title = getString(R.string.page_post_request_barang)
        }
    }

    private fun eventUI() {
        ivBarang?.setOnClickListener {
            DialogBuilder.ImageChooser(easyImage, this)
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
                        .into(ivBarang)
                }

                override fun onImagePickerError(
                    error: Throwable,
                    source: MediaSource
                ) {
                    //Some error handling
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) {
                    //Not necessary to remove any files manually anymore
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}