package com.rbc.demo.myapplication.model
import com.rbc.rbcaccountlibrary.Transaction
import java.util.*

sealed class TransactionListItemType {
    data class TypeHeader(val date: Calendar): TransactionListItemType()
    data class TypeTransaction(val transaction: Transaction) : TransactionListItemType()
}
