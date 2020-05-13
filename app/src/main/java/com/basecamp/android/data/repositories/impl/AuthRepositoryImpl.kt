package com.basecamp.android.data.repositories.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.common.extensions.safeCall
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.AuthDataSource
import com.basecamp.android.data.repositories.datasources.DDBBDataSource
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.User
import com.basecamp.android.domain.repositories.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Injectable(Scope.BY_USE)
class AuthRepositoryImpl(private val authDataSource: AuthDataSource, private val ddbbDataSource: DDBBDataSource, private val settingsPreferences: SettingsPreferences) : AuthRepository {

    override suspend fun forgotPassword(email: String): ResponseState<Void?> = authDataSource.forgotPassword(email)

    override suspend fun logIn(email: String, password: String): ResponseState<AuthResult?> = authDataSource.logIn(email, password).also {
        if (it is ResponseState.Success) settingsPreferences.setEmail(email)
    }

    override suspend fun signUp(name: String, email: String, password: String): ResponseState<Void> {
        return when (val response =  authDataSource.signUp(email, password)) {
            is ResponseState.Success -> {
                settingsPreferences.setEmail(email)
                response.result?.user?.uid?.let { //TODO
                    ddbbDataSource.createUser(email, User(name,email))
                } ?: safeCall { suspendCoroutine<Void> { cont -> cont.resumeWithException(Throwable("Something went wrong")) } } }
            else -> (response as ResponseState.Failure)
        }
    }


    override suspend fun logOut() = authDataSource.logOut()

    override suspend fun getCurrentUserMail(): String? = authDataSource.getCurrentUserMail()

}