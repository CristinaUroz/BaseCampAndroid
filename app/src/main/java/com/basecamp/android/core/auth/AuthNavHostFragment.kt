package com.basecamp.android.core.auth

import androidx.fragment.app.Fragment
import com.basecamp.android.R
import com.basecamp.android.core.auth.login.LogInScreen
import com.basecamp.android.core.auth.signup.SignUpScreen
import com.basecamp.android.core.common.BaseNavHostFragment

/**
 * Manages all "Auth" navigation flows.
 */
@Suppress("unused")
class AuthNavHostFragment : BaseNavHostFragment() {

    override fun applyNavigationRoutes(originFragment: Fragment) {
        when (originFragment) {
            is SignUpScreen -> {
                originFragment.onLogInClick = {
                    navigate(R.id.action_signup_screen_to_login_screen)
                }
            }
            is LogInScreen -> {
                originFragment.onForgotPasswordClick = {
                    navigate(R.id.action_login_screen_to_forgotpassword_screen)
                }
            }
        }
    }
}