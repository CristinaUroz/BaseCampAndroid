package com.basecamp.android.core.main.profile

import com.basecamp.android.R
import com.basecamp.android.core.Screen
import kotlin.reflect.KClass

class ProfileScreen : Screen<ProfilePresenter>(), ProfileContract.View, ProfileContract.Router {

    override fun getLayout(): Int  = R.layout.screen_profile

    override fun getPresenter(): KClass<ProfilePresenter> = ProfilePresenter::class

    override fun init() {}

}