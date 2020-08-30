package com.dinokeylas.jastipinaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinokeylas.jastipinaja.adapter.TransactionAdapter
import com.dinokeylas.jastipinaja.utils.GenerateData

class TransactionListFragment : Fragment() {

    val tranList = GenerateData.generateTransactionList()
    lateinit var recyclerView: RecyclerView
    lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.rv_transaction)
        transactionAdapter = TransactionAdapter(context!!, tranList)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = transactionAdapter

        return view
    }



}
