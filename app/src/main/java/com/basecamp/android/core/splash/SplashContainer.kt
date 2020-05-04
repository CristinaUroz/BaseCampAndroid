package com.basecamp.android.core.splash

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.basecamp.android.R
import com.basecamp.android.core.Container
import com.basecamp.android.core.auth.AuthContainer
import com.basecamp.android.core.common.BCGlide
import com.basecamp.android.core.main.MainContainer
import kotlin.reflect.KClass

class SplashContainer : Container<SplashPresenter>(), SplashContract.View, SplashContract.Router {

    private val image by lazy { findViewById<ImageView>(R.id.splash_pinroute_icon) }

    override fun getLayout(): Int = R.layout.container_splash

    override fun getPresenter(): KClass<SplashPresenter> = SplashPresenter::class

    override fun init() {
        BCGlide(this)
            .asGif()
            .load(R.raw.splash)
            .into(image)
    }

    override fun goToMain(bundle: Bundle) {
        val caller = Intent(this, MainContainer::class.java)
        caller.putExtras(bundle)
        startActivity(caller)
        finish()
    }

    override fun goToAuth(bundle: Bundle) {
        val caller = Intent(this, AuthContainer::class.java)
        caller.putExtras(bundle)
        startActivity(caller)
        finish()
    }

}