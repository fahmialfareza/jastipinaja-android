package com.dinokeylas.jastipinaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_post_choice.view.*

class PostChoiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_choice, container, false)

        view.tv_post_jastip.setOnClickListener {
            TODO("ad Intent here")
        }

        view.tv_request_jastip.setOnClickListener {
            TODO("ad Intent here")
        }

        return view
    }
}
