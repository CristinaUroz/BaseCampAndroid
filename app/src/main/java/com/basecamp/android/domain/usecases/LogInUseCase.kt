package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.repositories.AuthRepository
import com.google.firebase.auth.AuthResult

@Injectable(Scope.BY_USE)
class LogInUseCase(private val authRepository: AuthRepository) : UseCase {

    suspend fun logIn(email: String, password: String): ResponseState<AuthResult?> = authRepository.logIn(email, password)

    suspend fun logOut() = authRepository.logOut()

}