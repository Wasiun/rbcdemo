package com.rbc.demo.myapplication.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.rbc.demo.myapplication.R
import com.rbc.demo.myapplication.adapter.AccountListAdapter
import com.rbc.demo.myapplication.viewmodel.MainViewModel

class AccountFragment : Fragment(R.layout.fragment_accounts) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    lateinit var progressBar: CircularProgressIndicator
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById<CircularProgressIndicator>(R.id.progress_circular)
        val recyclerAdapter = AccountListAdapter() { accNumber, accName, accBalance, isCreditCard ->
            mainViewModel.accountNumber = accNumber
            mainViewModel.isCreditCard = isCreditCard
            mainViewModel.accountBalance = accBalance
            mainViewModel.accountName = accName
            parentFragmentManager.commit {
                addToBackStack(null)
                setReorderingAllowed(true)
                add<DetailsFragment>(R.id.container_fragment)
            }
        }

        view.findViewById<RecyclerView>(R.id.account_list_rv).apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = recyclerAdapter
        }
        mainViewModel.getAccounts()
        mainViewModel.accountList.observe(viewLifecycleOwner) { recyclerAdapter.setData(it) }
        mainViewModel.loading.observe(viewLifecycleOwner) {
            if (it)
                progressBar.visibility =
                    View.VISIBLE
            else progressBar.visibility =
                View.GONE

        }
    }
}