package com.rbc.demo.myapplication.model

import com.rbc.rbcaccountlibrary.Account

sealed class AccountListItemType {
    data class TypeHeader(val name: String): AccountListItemType()
    data class TypeAccount(val rbcAccount: Account) : AccountListItemType()
}