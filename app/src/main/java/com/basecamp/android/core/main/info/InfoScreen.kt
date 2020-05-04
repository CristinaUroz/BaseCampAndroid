package com.basecamp.android.core.main.info

import com.basecamp.android.R
import com.basecamp.android.core.Screen
import kotlin.reflect.KClass

class InfoScreen : Screen<InfoPresenter>(), InfoContract.View, InfoContract.Router {

    override fun getLayout(): Int = R.layout.screen_info

    override fun getPresenter(): KClass<InfoPresenter> = InfoPresenter::class

    override fun init() {}

}