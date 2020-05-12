package com.basecamp.android.data.repositories.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.common.extensions.safeCall
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.AuthDataSource
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.domain.models.User
import com.basecamp.android.domain.repositories.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Injectable(Scope.BY_USE)
class AuthRepositoryImpl(private val authDataSource: AuthDataSource, private val ddbbDataSource: DDBBDataSource) : AuthRepository {

    override suspend fun forgotPassword(email: String): ResponseState<Void?> = authDataSource.forgotPassword(email)

    override suspend fun logIn(email: String, password: String): ResponseState<AuthResult?> = authDataSource.logIn(email, password)

    override suspend fun signUp(name: String, email: String, password: String): ResponseState<String> {
        return when (val response =  authDataSource.signUp(email, password)) {
            is ResponseState.Success -> {
                response.result?.user?.uid?.let {
                    ddbbDataSource.createUser(it, User(name,email))
                } ?: safeCall { suspendCoroutine<String> { cont -> cont.resumeWithException(Throwable("Something went wrong")) } } }
            else -> (response as ResponseState.Failure)
        }
    }


    override suspend fun logOut() = authDataSource.logOut()

    override suspend fun getCurrentUserMail(): String? = authDataSource.getCurrentUserMail()

}