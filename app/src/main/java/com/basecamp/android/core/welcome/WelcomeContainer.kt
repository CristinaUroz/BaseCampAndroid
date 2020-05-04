package com.basecamp.android.core.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.basecamp.android.R
import com.basecamp.android.core.Container
import com.basecamp.android.core.main.MainContainer
import kotlin.reflect.KClass

class WelcomeContainer : Container<WelcomePresenter>(), WelcomeContract.View, WelcomeContract.Router {

    private val goBackButton by lazy {findViewById<Button>(R.id.container_welcome_back_button)}

    override fun getLayout(): Int = R.layout.container_welcome

    override fun getPresenter(): KClass<WelcomePresenter> = WelcomePresenter::class

    override fun init() {
        goBackButton.setOnClickListener {goToMain(Bundle())}
    }

    override fun goToMain(bundle: Bundle) {
        val caller = Intent(this, MainContainer::class.java)
        caller.putExtras(bundle)
        startActivity(caller)
        finish()
    }
}