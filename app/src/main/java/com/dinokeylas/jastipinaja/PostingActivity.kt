package com.dinokeylas.jastipinaja

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dinokeylas.jastipinaja.model.Post
import com.dinokeylas.jastipinaja.model.Product
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.POST
import com.dinokeylas.jastipinaja.utils.DateUtils
import com.dinokeylas.jastipinaja.utils.DialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_posting.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.util.*


class PostingActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    companion object {
        const val FLAG = "FLAG"
    }

    /*flag = 1 - Post Jastip
      flag = 2 - Post Request Barang*/
    private var flag = 0
    private lateinit var easyImage: EasyImage
    private var qty: Int = 0
    private var category: String = ""
    private var province: String = ""
    private var city: String = ""
    private var productImageUrl: String = ""
    private var hargaJastip: Int = 0
    private lateinit var date: Date

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
        //ToolBar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (flag == 1) {
            supportActionBar?.title = getString(R.string.page_post_jastip)
            etDeadlineBarang.hint = "Tanggal Belanja"
        } else {
            supportActionBar?.title = getString(R.string.page_post_request_barang)
            etDeadlineBarang.hint = "Tanggal Berakhir"
            til_harga_jastip?.visibility = View.GONE
        }

        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val arrayAdapterCategory = ArrayAdapter.createFromResource(
            this,
            R.array.category,
            android.R.layout.simple_spinner_item
        )

        val spinnerProvince = findViewById<Spinner>(R.id.spinnerProvince)
        val arrayAdapterProvince = ArrayAdapter.createFromResource(
            this,
            R.array.province,
            android.R.layout.simple_spinner_item
        )

        val spinnerCity = findViewById<Spinner>(R.id.spinnerCity)
        val arrayAdapterCity = ArrayAdapter.createFromResource(
            this,
            R.array.city,
            android.R.layout.simple_spinner_item
        )

        arrayAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = arrayAdapterCategory
        spinnerCategory.onItemSelectedListener = this

        arrayAdapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProvince.adapter = arrayAdapterProvince
        spinnerProvince.onItemSelectedListener = this

        arrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = arrayAdapterCity
        spinnerCity.onItemSelectedListener = this
    }

    private fun eventUI() {

        ivBarang?.setOnClickListener {
            DialogBuilder.ImageChooser(easyImage, this)
        }

        plusLyt.setOnClickListener {
            ++qty
            tvQty.text = qty.toString().trim()
        }

        minusLyt.setOnClickListener {
            if (qty == 0) {
                Toast.makeText(this, "Tidak boleh kurang dari nol", Toast.LENGTH_SHORT).show()
                Log.d("QTY", qty.toString())
            } else {
                --qty
                tvQty.text = qty.toString().trim()
            }
        }

        etDeadlineBarang?.setOnClickListener {
            showDatePickerDialog()
        }

        btnPost.setOnClickListener {
            val userId = getCurrentUser()
            val product = createProduct(userId)
            val post = createPost(product)
            saveToFirestore(post)
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
                    uploadImage(data?.data)
                }

                override fun onImagePickerError(
                    error: Throwable,
                    source: MediaSource
                ) {
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

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
        val item = adapterView?.getItemAtPosition(position).toString()
        when (adapterView?.id) {
            spinnerCategory.id -> category = item
            spinnerProvince.id -> province = item
            spinnerCity.id -> city = item
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun showDatePickerDialog() {
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val tempDate = Calendar.getInstance()
                tempDate[year, monthOfYear] = dayOfMonth
                date = tempDate.time
                etDeadlineBarang?.setText(DateUtils.getStringFormatedDate(date))
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    private fun uploadImage(uriProfileImage: Uri?) {
        progress_bar_product.visibility = View.VISIBLE
        val mStorageRef = FirebaseStorage.getInstance()
            .getReference("product/" + System.currentTimeMillis() + ".jpg")

        val uploadTask = mStorageRef.putFile(uriProfileImage!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            mStorageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                productImageUrl = downloadUri!!.toString()
            }
            progress_bar_product.visibility = View.GONE
        }
    }

    private fun getCurrentUser(): String {
        return FirebaseAuth.getInstance().currentUser?.uid.toString()
    }

    private fun createProduct(userId: String): Product {
        var shoppingDate: Date = Date()
        var expiredDate: Date = Date()
        if (flag == 1) {
            shoppingDate = date
            hargaJastip = etHargaJastip?.text.toString().toInt()
        } else {
            expiredDate = date
        }
        return Product(
            "",
            userId,
            etNamaBarang.text.toString(),
            etDeskripsiBarang.text.toString(),
            category,
            etHargaBarang.text.toString().toInt(),
            tvQty.text.toString().toInt() ?: 0,
            hargaJastip,
            0,
            productImageUrl,
            city,
            province,
            etLokasiBarang.text.toString(),
            etKeteranganBarang.text.toString(),
            shoppingDate,
            expiredDate,
            Calendar.getInstance().time,
            Calendar.getInstance().time
        )
    }

    private fun createPost(product: Product): Post {
        return Post(
            "",
            product,
            etNamaBarang.text.toString() ?: "",
            product.productOwner,
            flag,
            false,
            Calendar.getInstance().time,
            Calendar.getInstance().time
        )
    }

    private fun saveToFirestore(post: Post) {
        progress_bar_product.visibility = View.VISIBLE
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection(POST).add(post).addOnSuccessListener {
            progress_bar_product.visibility = View.GONE
            showInformationDialog()
        }.addOnFailureListener {
            progress_bar_product.visibility = View.GONE
            Toast.makeText(this, "Gagal memposting", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showInformationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informasi")
        builder.setMessage("Yey! Posting berhasil!")
        builder.setPositiveButton("Oke") { _, _ ->
            finish()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}