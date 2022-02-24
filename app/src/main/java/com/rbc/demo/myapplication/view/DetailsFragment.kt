package com.rbc.demo.myapplication.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.rbc.demo.myapplication.R
import com.rbc.demo.myapplication.adapter.TransactionAdapter
import com.rbc.demo.myapplication.viewmodel.MainViewModel

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val adapterTransaction by lazy {
        TransactionAdapter()
    }
    lateinit var progress: CircularProgressIndicator
    lateinit var errorText: TextView
    private val viewModel by activityViewModels<MainViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress = view.findViewById(R.id.progress_circular)
        errorText = view.findViewById(R.id.error_txt)
        view.findViewById<TextView>(R.id.acnt_name_text).text = viewModel.accountName
        view.findViewById<TextView>(R.id.acnt_number_text).text = viewModel.accountNumber
        view.findViewById<TextView>(R.id.acnt_balance_text).text =
            "Total  " + viewModel.accountBalance
        setupRV(view)
        viewModel.getTransactions()
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) progress.visibility = View.VISIBLE
            else progress.visibility = View.GONE
        }
        viewModel.transactionList.observe(viewLifecycleOwner) {
            adapterTransaction.setData(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it) errorText.visibility = View.VISIBLE
            else errorText.visibility = View.GONE
        }
    }

    private fun setupRV(view: View) {
        view.findViewById<RecyclerView>(R.id.transaction_list_rv).apply {
            layoutManager = LinearLayoutManager(
                activity, RecyclerView.VERTICAL, false
            )
            adapter = adapterTransaction
        }
    }
}