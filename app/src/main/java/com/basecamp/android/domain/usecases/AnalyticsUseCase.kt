package com.basecamp.android.domain.usecases

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.domain.UseCase
import com.basecamp.android.domain.repositories.AnalyticsRepository

@Injectable(Scope.BY_USE)
class AnalyticsUseCase(private val analyticsRepository: AnalyticsRepository) : UseCase {

    fun setScreen(pageName: String) {
        return analyticsRepository.setScreen(pageName)
    }

    fun trackLogin() {
        return analyticsRepository.trackLogin()
    }

    fun trackRegister() {
        return analyticsRepository.trackRegister()
    }

    fun nonFatal(throwable: Throwable) {
        return analyticsRepository.nonFatal(throwable)
    }

    fun setUserEmail(email: String) {
        return analyticsRepository.setUserEmail(email)
    }

    fun setUserIdentifier(userId: String) {
        return analyticsRepository.setUserIdentifier(userId)
    }

}