package com.basecamp.android.data.datasources.analytics.impl

import android.content.Context
import android.os.Bundle
import android.util.Log
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.repositories.datasources.AnalyticsDataSource
import com.google.firebase.analytics.FirebaseAnalytics

@Injectable(Scope.BY_USE)
class AnalyticsApiDataSource(context: Context) : AnalyticsDataSource {
    private val firebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }


    override fun setUserId(userId: String) {
        try {
            firebaseAnalytics.setUserId(userId)
        } catch (e: Exception) {
        }
    }

    override fun setUserMail(mail: String) {
        try {
            firebaseAnalytics.setUserProperty("mail", mail)
        } catch (e: Exception) {
        }

    }

    override fun setScreen(screen: String) {
        Log.i("CRISS", screen)
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, screen)
            firebaseAnalytics.logEvent("screen", bundle)
        } catch (e: Exception) {
        }
    }

    override fun trackLogin() {
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.METHOD, "pinroute")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
        } catch (e: Exception) {
        }
    }

    override fun trackRegister() {
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.METHOD, "pinroute")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
        } catch (e: Exception) {
        }
    }

}