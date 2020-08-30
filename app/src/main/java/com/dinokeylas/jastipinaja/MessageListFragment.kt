package com.dinokeylas.jastipinaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinokeylas.jastipinaja.adapter.MessageAdapter
import com.dinokeylas.jastipinaja.utils.GenerateData

class MessageListFragment : Fragment() {

    val messageList = GenerateData.generateMessageList()
    lateinit var recyclerView: RecyclerView
    lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message_list, container, false)

        recyclerView = view.findViewById(R.id.rv_message)
        messageAdapter = MessageAdapter(context!!, messageList)

        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = messageAdapter

        return view
    }

}
