package com.basecamp.android.core.common

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

/**
 * Defines the method which will manage the navigation between UI components ([Fragment]s mainly).
 */
abstract class BaseNavHostFragment : NavHostFragment() {

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        applyNavigationRoutes(fragment)
    }

    /**
     * Manages all the navigation flows taking into account that the [originFragment] is the one
     * which will be navigating somewhere and we must indicate on this method when, where and how.
     */
    protected abstract fun applyNavigationRoutes(originFragment: Fragment)

    /**
     * Navigates following the provided [routeId] and passing the [data].
     */
    protected fun navigate(@IdRes routeId: Int, data: Bundle = Bundle.EMPTY) {
        try {
            navController.navigate(routeId, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
