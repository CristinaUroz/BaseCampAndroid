package com.basecamp.android.core.main.profile

import android.widget.Button
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import kotlin.reflect.KClass

class ProfileScreen : Screen<ProfilePresenter>(), ProfileContract.View, ProfileContract.Router {

    private val logOutButton by lazy { findViewById<Button>(R.id.screen_profile_logout_button) }

    override fun getLayout(): Int = R.layout.screen_profile

    override fun getPresenter(): KClass<ProfilePresenter> = ProfilePresenter::class

    override fun init() {
        logOutButton.setOnClickListener {
            notify { onLogOutClick() }
        }
    }

}