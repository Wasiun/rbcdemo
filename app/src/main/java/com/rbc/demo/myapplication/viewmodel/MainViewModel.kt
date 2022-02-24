package com.rbc.demo.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbc.demo.myapplication.model.AccountListItemType
import com.rbc.demo.myapplication.model.TransactionListItemType
import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountProvider
import com.rbc.rbcaccountlibrary.AccountType
import com.rbc.rbcaccountlibrary.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    var accountName = ""
    var accountNumber = ""
    var accountBalance = ""
    var isCreditCard: Boolean = false

    private val transList = ArrayList<TransactionListItemType>()
    private val _transactionList = MutableLiveData<ArrayList<TransactionListItemType>>()
    val transactionList: LiveData<ArrayList<TransactionListItemType>>
        get() = _transactionList
    private val _finalList = MutableLiveData<ArrayList<AccountListItemType>>()
    val accountList: LiveData<ArrayList<AccountListItemType>>
        get() = _finalList
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    fun getAccounts() {
        _loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val accountList = AccountProvider.getAccountsList()
                val groupedList = accountList.groupBy { it.type }
                _finalList.postValue(listForDisplay(groupedList))
            }
            _loading.postValue(false)
        }
    }

    private fun listForDisplay(inputList: Map<AccountType, List<Account>>): ArrayList<AccountListItemType> {
        val groupedList = ArrayList<AccountListItemType>()
        inputList.forEach { (accountType, list) ->
            groupedList.add(AccountListItemType.TypeHeader(list[0].name))
            list.forEach {
                groupedList.add(AccountListItemType.TypeAccount(it))
            }
        }
        return groupedList
    }

    fun getTransactions() {
        transList.clear()
        _loading.value = true
        _error.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allTrans = ArrayList<Transaction>()
                val allAccTrans = async {
                    try {
                        AccountProvider.getTransactions(accountNumber).toMutableList()
                    } catch (exception: java.lang.Exception) {
                        null
                    }
                }
                if (isCreditCard) {
                    val addCreditTrans = async {
                        try {
                            AccountProvider.getAdditionalCreditCardTransactions(accountNumber)
                        } catch (exception: java.lang.Exception) {
                            null
                        }
                    }
                    addCreditTrans.await()?.let {
                        allTrans.addAll(it)
                    }
                }

                allAccTrans.await()?.let {
                    allTrans.addAll(it)
                }

                val allMaps =
                    allTrans.sortedByDescending { it.date.timeInMillis }.groupBy { it.date }
                allMaps.forEach { (date, items) ->
                    transList.add(TransactionListItemType.TypeHeader(date))
                    items.forEach {
                        transList.add(TransactionListItemType.TypeTransaction(it))
                    }
                }
                if (transList.size == 0) {
                    _transactionList.postValue(transList)
                    _error.postValue(true)
                } else {
                    _transactionList.postValue(transList)
                    _error.postValue(false)
                }
                _loading.postValue(false)
            }
        }
    }
}