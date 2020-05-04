package com.basecamp.android.core.auth.login

import android.widget.Button
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.auth.signup.SignUpContract
import com.basecamp.android.core.auth.signup.SignUpPresenter
import kotlin.reflect.KClass

class LogInScreen : Screen<SignUpPresenter>(), SignUpContract.View, SignUpContract.Router {

    private val forgotPasswordButton by lazy {findViewById<Button>(R.id.screen_login_forgot_password_button)}
    var onForgotPasswordClick: () -> Unit = {}

    override fun getLayout(): Int = R.layout.screen_login

    override fun getPresenter(): KClass<SignUpPresenter> = SignUpPresenter::class

    override fun init() {
        forgotPasswordButton.setOnClickListener { onForgotPasswordClick.invoke() }
    }

}