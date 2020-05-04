package com.basecamp.android.core.auth.forgotpassword

import android.widget.Button
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.common.closeFragment
import kotlin.reflect.KClass

class ForgotPasswordScreen : Screen<ForgotPasswordPresenter>(), ForgotPasswordContract.View, ForgotPasswordContract.Router {


    private val close by lazy {findViewById< Button>(R.id.screen_forgotpassword_login_button)}


    override fun getLayout(): Int = R.layout.screen_forgotpassword

    override fun getPresenter(): KClass<ForgotPasswordPresenter> = ForgotPasswordPresenter::class

    override fun init() {
        close.setOnClickListener { closeFragment() }
    }
}