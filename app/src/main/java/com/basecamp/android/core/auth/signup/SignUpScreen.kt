package com.basecamp.android.core.auth.signup

import android.os.Bundle
import android.widget.Button
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import kotlin.reflect.KClass

class SignUpScreen : Screen<SignUpPresenter>(), SignUpContract.View, SignUpContract.Router {

    private val logInButton by lazy { findViewById<Button>(R.id.screen_signup_login_button)}
    var onLogInClick: () -> Unit = {}

    override fun getLayout(): Int = R.layout.screen_signup

    override fun getPresenter(): KClass<SignUpPresenter> = SignUpPresenter::class

    override fun init() {
        logInButton.setOnClickListener {
            notify { goToMainContainer(Bundle()) }
//            onLogInClick.invoke()
        }
    }

}
