package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.repositories.AuthRepository

@Injectable(Scope.BY_USE)
class ForgotPassowrdUseCase(private val authRepository: AuthRepository) : UseCase {

    suspend fun forgotPassword(email: String): ResponseState<Void?> = authRepository.forgotPassword(email)

}