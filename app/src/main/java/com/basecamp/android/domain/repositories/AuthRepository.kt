package com.basecamp.android.domain.repositories

import com.basecamp.android.data.datasources.ResponseState
import com.google.firebase.auth.AuthResult

interface AuthRepository {

    suspend fun forgotPassword(email: String): ResponseState<Void?>

    suspend fun logIn(email: String, password: String): ResponseState<AuthResult?>

    suspend fun signUp(name: String, email: String, password: String): ResponseState<Void>

    suspend fun logOut()

    suspend fun getCurrentUserMail(): String?

}