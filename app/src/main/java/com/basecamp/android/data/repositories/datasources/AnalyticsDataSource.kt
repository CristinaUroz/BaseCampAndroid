package com.basecamp.android.data.repositories.datasources

interface AnalyticsDataSource {

    fun setUserId(userId: String)

    fun setUserMail(mail: String)

    fun setScreen(screen: String)

    fun trackLogin()

    fun trackRegister()

}