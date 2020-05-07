package com.basecamp.android.core.common.extensions

import com.basecamp.android.data.datasources.ResponseState

suspend fun <T> safeCall(call: suspend () -> T): ResponseState<T> =
    try {
        val response = call()
        ResponseState.Success(response)
    } catch (ex: Exception) {
        ResponseState.Failure(ex)
    }