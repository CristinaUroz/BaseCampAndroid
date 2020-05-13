package com.basecamp.android.core.main.profile

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.common.extensions.BCGlide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlin.reflect.KClass

class ProfileScreen : Screen<ProfilePresenter>(), ProfileContract.View, ProfileContract.Router {

    private val nameTextView by lazy { findViewById<TextView>(R.id.screen_profile_name) }
    private val emailTextView by lazy { findViewById<TextView>(R.id.screen_profile_email) }
    private val descriptionTextView by lazy { findViewById<TextView>(R.id.screen_profile_description) }
    private val pictureImageView by lazy { findViewById<ImageView>(R.id.screen_profile_picture) }
    private val logOutButton by lazy { findViewById<TextView>(R.id.screen_profile_logout_button) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_profile_progress_bar) }
    private val errorView by lazy { findViewById<LinearLayout>(R.id.screen_profile_error) }
    private val editView by lazy { findViewById<ImageView>(R.id.screen_profile_edit) }

    override fun getLayout(): Int = R.layout.screen_profile

    override fun getPresenter(): KClass<ProfilePresenter> = ProfilePresenter::class

    override fun init() {
        logOutButton.setOnClickListener {
            notify { onLogOutClick() }
        }
    }

    override fun showProgressBar(b: Boolean){
        progressBar.visibility = if (b) View.VISIBLE else View.INVISIBLE
    }

    override fun setName(name: String) {
        editView.visibility = View.VISIBLE
        nameTextView.text = name
    }

    override fun setDescription(description: String) {
        descriptionTextView.text = description
    }

    override fun setEmail(email: String) {
        emailTextView.text = email
    }

    override fun setPicture(picture: String?) {
        context?.let {
            BCGlide(it)
                .load(picture ?: R.drawable.default_profile)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(50)))
                .into(pictureImageView)
        }
    }

    override fun showError(b: Boolean) {
        errorView.visibility = if (b) {showProgressBar(false); View.VISIBLE} else View.GONE
    }

}