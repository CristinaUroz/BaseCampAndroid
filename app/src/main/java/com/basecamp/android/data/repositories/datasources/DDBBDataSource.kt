package com.basecamp.android.data.repositories.datasources

import com.basecamp.android.data.datasources.DataSource
import com.basecamp.android.data.datasources.ResponseState
import com.google.firebase.auth.AuthResult

interface DDBBDataSource : DataSource {

    suspend fun forgotPassword(email: String): ResponseState<Void?>

    suspend fun logIn(email: String, password: String): ResponseState<AuthResult?>

    suspend fun signUp(email: String, password: String): ResponseState<AuthResult?>

    suspend fun logOut()

    suspend fun getCurrentUserMail(): String?

}