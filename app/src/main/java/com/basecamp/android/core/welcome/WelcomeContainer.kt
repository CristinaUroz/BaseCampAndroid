package com.basecamp.android.core.welcome

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.basecamp.android.R
import com.basecamp.android.core.Container
import com.basecamp.android.core.common.extensions.BCGlide
import com.basecamp.android.core.common.views.BCVideo
import com.basecamp.android.core.main.MainContainer
import com.basecamp.android.domain.models.MafiaWelcome
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlin.reflect.KClass

class WelcomeContainer : Container<WelcomePresenter>(), WelcomeContract.View, WelcomeContract.Router {

    private val picture by lazy { findViewById<ImageView>(R.id.container_welcome_picture) }
    private val title by lazy { findViewById<TextView>(R.id.container_welcome_title) }
    private val text by lazy { findViewById<TextView>(R.id.container_welcome_text) }
    private val video by lazy { findViewById<BCVideo>(R.id.container_welcome_video) }
    private val goBackButton by lazy { findViewById<TextView>(R.id.container_welcome_back_button) }

    override fun getLayout(): Int = R.layout.container_welcome

    override fun getPresenter(): KClass<WelcomePresenter> = WelcomePresenter::class

    override fun init() {
        goBackButton.setOnClickListener { goToMain(Bundle()) }
    }

    override fun goToMain(bundle: Bundle) {
        val caller = Intent(this, MainContainer::class.java)
        caller.putExtras(bundle)
        startActivity(caller)
        finish()
    }

    override fun setWelcome(welcome: MafiaWelcome) {

        welcome.picture?.takeIf { it != "" }?.let { pic ->
            BCGlide(this)
                .load(pic)
                .apply(RequestOptions().transform(RoundedCorners(80)))
                .into(picture)

        }

        welcome.video?.let {
            video.setVideo(it)
        } ?: video.apply{ visibility = View.GONE}


        text.movementMethod = LinkMovementMethod.getInstance()
        text.text = welcome.text ?: ""
        Linkify.addLinks(text, Linkify.WEB_URLS)

        title.text = welcome.title ?: ""
    }

    override fun setError() {
    }
}