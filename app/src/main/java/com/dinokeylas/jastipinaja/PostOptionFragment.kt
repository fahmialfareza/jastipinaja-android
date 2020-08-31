package com.dinokeylas.jastipinaja

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_post_option.*

class PostOptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_option, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_post_jastip?.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    PostingActivity::class.java
                ).putExtra(PostingActivity.FLAG, 1)
            )
        }
        tv_request_jastip?.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    PostingActivity::class.java
                ).putExtra(PostingActivity.FLAG, 2)
            )
        }
    }
}
