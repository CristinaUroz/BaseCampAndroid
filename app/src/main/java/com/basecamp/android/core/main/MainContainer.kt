package com.basecamp.android.core.main

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.basecamp.android.R
import com.basecamp.android.core.Container
import kotlinx.android.synthetic.main.container_main.*
import kotlin.reflect.KClass

class MainContainer : Container<MainPresenter>(), MainContract.View, MainContract.Router {

    override fun getLayout(): Int  = R.layout.container_main

    override fun getPresenter(): KClass<MainPresenter> = MainPresenter::class

    override fun init() {
        setupViews()
    }

    private fun setupViews() {
        val navController = findNavController(R.id.main_navigation_host_fragment)
        bottomNav.setupWithNavController(navController)
    }
}