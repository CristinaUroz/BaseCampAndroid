package com.basecamp.android.core.main.feed

import com.basecamp.android.R
import com.basecamp.android.core.Screen
import kotlin.reflect.KClass

class FeedScreen : Screen<FeedPresenter>(), FeedContract.View, FeedContract.Router {

    override fun getLayout(): Int = R.layout.screen_feed

    override fun getPresenter(): KClass<FeedPresenter> = FeedPresenter::class

    override fun init() {}

}