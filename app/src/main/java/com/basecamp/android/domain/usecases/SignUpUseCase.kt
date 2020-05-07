package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.repositories.AuthRepository
import com.google.firebase.auth.AuthResult

@Injectable(Scope.BY_USE)
class SignUpUseCase(private val authRepository: AuthRepository) : UseCase {

    suspend fun signUp(email: String, password: String): ResponseState<AuthResult?> = authRepository.signUp(email, password)

}