package com.basecamp.android.data.repositories.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.repositories.AuthRepository
import com.google.firebase.auth.AuthResult

@Injectable(Scope.BY_USE)
class AuthRepositoryImpl(private val ddbbDataSource: DDBBDataSource) : AuthRepository {

    override suspend fun forgotPassword(email: String): ResponseState<Void?> = ddbbDataSource.forgotPassword(email)

    override suspend fun logIn(email: String, password: String): ResponseState<AuthResult?> = ddbbDataSource.logIn(email, password)

    override suspend fun signUp(email: String, password: String): ResponseState<AuthResult?> = ddbbDataSource.signUp(email, password)

    override suspend fun logOut() = ddbbDataSource.logOut()

    override suspend fun getCurrentUserMail(): String? = ddbbDataSource.getCurrentUserMail()

}