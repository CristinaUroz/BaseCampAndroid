package com.basecamp.android.core.auth

import android.content.Intent
import android.os.Bundle
import com.basecamp.android.R
import com.basecamp.android.core.Container
import com.basecamp.android.core.main.MainContainer
import kotlin.reflect.KClass

class AuthContainer : Container<AuthPresenter>(), AuthContract.View, AuthContract.Router {

    override fun getLayout(): Int = R.layout.container_auth

    override fun getPresenter(): KClass<AuthPresenter> = AuthPresenter::class

    override fun init() {}

    override fun goToMain(bundle: Bundle) {
        val caller = Intent(this, MainContainer::class.java)
        caller.putExtras(bundle)
        startActivity(caller)
        finish()
    }
}