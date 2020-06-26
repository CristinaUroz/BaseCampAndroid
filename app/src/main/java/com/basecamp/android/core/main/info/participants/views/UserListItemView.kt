package com.basecamp.android.core.main.info.participants.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import cc.popkorn.inject
import com.basecamp.android.R
import com.basecamp.android.core.common.extensions.BCGlide
import com.basecamp.android.core.common.extensions.getGroupName
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.User

class UserListItemView(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    val profilePicture by lazy { findViewById<ImageView>(R.id.view_user_item_list_picture) }
    private val name by lazy { findViewById<TextView>(R.id.view_user_item_list_name) }
    private val family by lazy { findViewById<TextView>(R.id.view_user_item_list_family) }
    private val settingsPreferences = inject<SettingsPreferences>()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_item_list, this, true)
    }

    fun setUser(user: User) {
        profilePicture.transitionName = user.email
        val picture =
            if (settingsPreferences.getDarkMode()) {
                name.text = user.alias ?: ""
                user.group?.let {
                    family.text = it.getGroupName()
                    family.visibility = View.VISIBLE
                } ?: family.apply { visibility = View.GONE }
                user.imageM
            } else {
                name.text = user.name ?: ""
                family.visibility = View.GONE
                user.image
            }

        context?.let {
            picture?.takeIf { picture != "" }?.let { pic ->
                BCGlide(it)
                    .load(picture)
                    .centerCrop()
                    .into(profilePicture)
            } ?: R.drawable.ic_default_profile_normal
                .takeIf { !settingsPreferences.getDarkMode() }
                ?.let {
                    profilePicture.setImageResource(it) }
            ?: R.drawable.ic_default_profile_mafia
                .let {
                    profilePicture.setImageResource(it) }
        }
    }
}
