package com.basecamp.android.data.datasources

sealed class ResponseState<out T> {
    data class Success<T>(val result: T): ResponseState<T>() {}
    data class Failure(val ex: Exception): ResponseState<Nothing>()
}