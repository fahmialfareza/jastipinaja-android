package com.dinokeylas.jastipinaja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinokeylas.jastipinaja.adapter.PostAdapter
import com.dinokeylas.jastipinaja.model.Post
import com.dinokeylas.jastipinaja.utils.Constant.Collections.Companion.POST
import com.dinokeylas.jastipinaja.utils.GridItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_post_all.*

class PostAllActivity : AppCompatActivity() {

    companion object {
        const val FLAG = "FLAG"
    }

    /*flag = 1 - Post Jastip
      flag = 2 - Post Request Barang*/
    private var flag = 0
    private var postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_all)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
    }

    private fun initObject() {
        flag = intent.getIntExtra(PostingActivity.FLAG, 0)
        initPostList()
    }

    private fun initUI() {
        //ToolBar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (flag == 1) {
            supportActionBar?.title = getString(R.string.page_post_jastip_all)
        } else {
            supportActionBar?.title = getString(R.string.page_post_request_barang_all)
        }

        //RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.rv_all_post)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.addItemDecoration(GridItemDecoration(10, 2))

        val adapter = PostAdapter(this, postList)
        recyclerView.adapter = adapter
    }

    private fun eventUI() {

    }

    private fun initPostList(){
        FirebaseFirestore.getInstance().collection(POST).whereEqualTo("postType", flag).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val post: Post = document.toObject(Post::class.java)
                    post.postId = document.id
                    postList.add(post)
                }
                initUI()
            }.addOnFailureListener { Log.e("FETCH-POST", it.message) }
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
