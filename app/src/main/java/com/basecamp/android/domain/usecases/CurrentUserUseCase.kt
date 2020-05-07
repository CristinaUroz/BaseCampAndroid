package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.repositories.AuthRepository

@Injectable(Scope.BY_USE)
class CurrentUserUseCase(private val authRepository: AuthRepository) : UseCase {

    suspend fun getCurrentUserMail(): String? {
        return authRepository.getCurrentUserMail()
    }

}