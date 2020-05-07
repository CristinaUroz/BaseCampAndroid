package com.basecamp.android.core.main

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.basecamp.android.R
import com.basecamp.android.core.Container
import com.basecamp.android.core.splash.SplashContainer
import com.basecamp.android.core.welcome.WelcomeContainer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.container_main.*
import kotlin.reflect.KClass

class MainContainer : Container<MainPresenter>(), MainContract.View, MainContract.Router {

    private val darkModeButton by lazy { findViewById<FloatingActionButton>(R.id.enable_dark_mode) }

    override fun getLayout(): Int = R.layout.container_main

    override fun getPresenter(): KClass<MainPresenter> = MainPresenter::class

    override fun init() {
        setupViews()
        darkModeButton.setOnClickListener {
            notify { onDarkModeClick() }
        }
    }

    private fun setupViews() {
        val navController = findNavController(R.id.main_navigation_host_fragment)
        bottomNav.setupWithNavController(navController)
    }

    override fun showFloatingButton(b: Boolean) {
        if (b) darkModeButton.show()
        else darkModeButton.hide()
    }

    override fun goToWelcome(bundle: Bundle) {
        val caller = Intent(this, WelcomeContainer::class.java)
        caller.putExtras(bundle)
        startActivity(caller)
        finish()
    }
    override fun goToSplash(bundle: Bundle) {
        val caller = Intent(this, SplashContainer::class.java)
        caller.putExtras(bundle)
        startActivity(caller)
        finish()
    }

}