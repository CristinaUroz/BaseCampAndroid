package com.basecamp.android.data.datasources.analytics.impl

import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.repositories.datasources.CrashlyticsDataSource
import com.crashlytics.android.Crashlytics

@Injectable(Scope.BY_USE)
class CrashlyticsApiDataSource : CrashlyticsDataSource {

    override fun setScreen(screen: String) {
        try {
            Crashlytics.setString("screen", screen)
            Crashlytics.log("screen: $screen")
        } catch (e: Exception) {
        }
    }

    override fun nonFatal(throwable: Throwable) {
        try {
            Crashlytics.logException(throwable)
        } catch (e: Exception) {
        }
    }

    override fun setUserIdentifier(userId: String) {
        try {
            Crashlytics.setUserIdentifier(userId)
        } catch (e: Exception) {
        }
    }

    override fun setUserEmail(email: String) {
        try {
            Crashlytics.setUserEmail(email)
        } catch (e: Exception) {
        }
    }

}