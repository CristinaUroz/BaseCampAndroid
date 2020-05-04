package com.basecamp.android.data.repositories.datasources

import com.basecamp.android.data.datasources.DataSource

interface CrashlyticsDataSource : DataSource {

    fun setScreen(screen: String)

    fun nonFatal(throwable: Throwable)

    fun setUserIdentifier(userId: String)

    fun setUserEmail(email: String)

}