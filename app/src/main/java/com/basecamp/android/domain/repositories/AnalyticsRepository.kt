package com.basecamp.android.domain.repositories

interface AnalyticsRepository {

    fun setScreen(pageName: String)

    fun trackLogin()

    fun trackRegister()

    fun nonFatal(throwable: Throwable)

    fun setUserEmail(email: String)

    fun setUserIdentifier(userId: String)
}