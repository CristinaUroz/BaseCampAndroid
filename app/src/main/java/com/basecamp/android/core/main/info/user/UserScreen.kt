package com.basecamp.android.core.main.info.user

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import cc.popkorn.inject
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.common.extensions.getGroupName
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlin.reflect.KClass

class UserScreen : Screen<UserPresenter>(), UserContract.View, UserContract.Router {

    private val data: UserScreenArgs by navArgs()

    private val nameTextView by lazy { findViewById<TextView>(R.id.screen_profile_name) }
    private val emailTextView by lazy { findViewById<TextView>(R.id.screen_profile_email) }
    private val descriptionTextView by lazy { findViewById<TextView>(R.id.screen_profile_description) }
    private val pictureImageView by lazy { findViewById<ImageView>(R.id.screen_profile_picture) }
    private val logOutButton by lazy { findViewById<TextView>(R.id.screen_profile_logout_button) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_profile_progress_bar) }
    private val errorView by lazy { findViewById<LinearLayout>(R.id.screen_profile_error) }

    private val settingsPreferences = inject<SettingsPreferences>()

    override fun getLayout(): Int = R.layout.screen_profile

    override fun getPresenter(): KClass<UserPresenter> = UserPresenter::class

    override fun init() {
        logOutButton.visibility = View.GONE
        emailTextView.visibility = View.GONE
        notify { onUserRecieved(data.user) }
        descriptionTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSharedElementTransitionOnEnter()
        postponeEnterTransition()

        pictureImageView.apply {
            transitionName = data.user.email
            startEnterTransitionAfterLoadingImage(data.user.image.takeIf { !settingsPreferences.getDarkMode() } ?: data.user.imageM, this)
        }
    }

    override fun showProgressBar(b: Boolean) {
        progressBar.visibility = if (b) View.VISIBLE else View.INVISIBLE
    }


    override fun setName(name: String) {
        nameTextView.text = name
    }

    override fun setDescription(description: String) {
        descriptionTextView.text = description
        Linkify.addLinks(descriptionTextView, Linkify.WEB_URLS)
    }


    override fun setGroup(group: Int) {
        emailTextView.visibility = View.VISIBLE
        emailTextView.text = group.getGroupName()
    }

    override fun showError(b: Boolean) {
        errorView.visibility = if (b) {
            showProgressBar(false); View.VISIBLE
        } else View.GONE
    }


    private fun setSharedElementTransitionOnEnter() {
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_element_transition)
    }

    private fun startEnterTransitionAfterLoadingImage(imageAddress: String?, imageView: ImageView) {
        Glide.with(this)
            .load(imageAddress ?: R.drawable.ic_default_profile_normal
                .takeIf { !settingsPreferences.getDarkMode() } ?: R.drawable.ic_default_profile_mafia)
            .apply(RequestOptions().transform(RoundedCorners(80)))
            .dontAnimate()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(imageView)
    }
}