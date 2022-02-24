package com.rbc.demo.myapplication.model

sealed class Result<out T> {
    data class Success<out R>(val value: R): Result<R>()
    data class Loading(val state: Boolean): Result<Boolean>()
}
