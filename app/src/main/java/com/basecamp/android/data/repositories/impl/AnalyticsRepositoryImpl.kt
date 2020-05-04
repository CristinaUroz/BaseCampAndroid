package com.basecamp.android.data.repositories.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.repositories.datasources.AnalyticsDataSource
import com.basecamp.android.data.repositories.datasources.CrashlyticsDataSource
import com.basecamp.android.domain.repositories.AnalyticsRepository

@Injectable(Scope.BY_USE)
class AnalyticsRepositoryImpl(private val crashlyticsDataSource: CrashlyticsDataSource, private val analyticsRepository: AnalyticsDataSource) :
    AnalyticsRepository {

    override fun setScreen(pageName: String) {
        crashlyticsDataSource.setScreen(pageName)
        analyticsRepository.setScreen(pageName)
    }

    override fun trackLogin() {
        analyticsRepository.trackLogin()
    }

    override fun trackRegister() {
        analyticsRepository.trackRegister()
    }

    override fun nonFatal(throwable: Throwable) {
        crashlyticsDataSource.nonFatal(throwable)
    }

    override fun setUserEmail(email: String) {
        crashlyticsDataSource.setUserEmail(email)
        analyticsRepository.setUserMail(email)
    }

    override fun setUserIdentifier(userId: String) {
        crashlyticsDataSource.setUserIdentifier(userId)
        analyticsRepository.setUserId(userId)
    }

}