package com.rbc.demo.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rbc.demo.myapplication.R
import com.rbc.demo.myapplication.model.AccountListItemType
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType

class AccountListAdapter(private val click: (String, String, String, Boolean) -> Unit) :
    RecyclerView.Adapter<AccountListAdapter.AccountViewHolder>() {
    private val items = ArrayList<AccountListItemType>()

    fun setData(items: List<AccountListItemType>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class AccountViewHolder(
        private val view: View,
        val click: (String, String, String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        fun bind(type: Int, value: Any) {
            if (type == Type.HEADER) bindHeader(value as String)
            if (type == Type.ACCOUNT) bindAccount(value as Account)
        }

        private fun bindHeader(title: String) {
            view.findViewById<TextView>(R.id.header_text).text = title
        }

        private fun bindAccount(account: Account) {
            view.findViewById<TextView>(R.id.account_name).text = account.number
            view.findViewById<TextView>(R.id.account_balance).text = account.balance
            view.findViewById<View>(R.id.view_account).setOnClickListener {
                click(
                    account.number,
                    account.name,
                    account.balance,
                    account.type == AccountType.CREDIT_CARD
                )
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountViewHolder {
        val view = if (viewType == Type.HEADER)
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_header_account, parent, false)
        else LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_account, parent, false)
        return AccountViewHolder(view, click)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        if (items[position] is AccountListItemType.TypeAccount)
            holder.bind(Type.ACCOUNT, items[position].run {
                this as AccountListItemType.TypeAccount
                rbcAccount
            })
        else {
            holder.bind(Type.HEADER, items[position].run {
                this as AccountListItemType.TypeHeader
                name
            })
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AccountListItemType.TypeHeader -> Type.HEADER
            is AccountListItemType.TypeAccount -> Type.ACCOUNT
        }
    }
}