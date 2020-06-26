package com.basecamp.android.core.main.info.families.views

import android.content.Context
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.basecamp.android.R
import com.basecamp.android.core.common.extensions.getGroupLogo
import com.basecamp.android.core.common.extensions.getGroupName
import com.basecamp.android.core.common.extensions.getLeaderName
import com.basecamp.android.domain.models.Family

class FamilyView(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {

    private val nameTextView by lazy { findViewById<TextView>(R.id.view_family_item_name) }
    private val leaderTextView by lazy { findViewById<TextView>(R.id.view_family_item_leader) }
    private val descriptionTextView by lazy { findViewById<TextView>(R.id.view_family_item_description) }
    private val pictureImageView by lazy { findViewById<ImageView>(R.id.view_family_item_picture) }


    init {
        LayoutInflater.from(context).inflate(R.layout.view_family_item, this, true)
    }

    fun setFamily(family: Family) {

        descriptionTextView.movementMethod = LinkMovementMethod.getInstance()

        family.familyId.apply {
            pictureImageView.setImageResource(getGroupLogo())
            nameTextView.text= getGroupName()
            leaderTextView.text = resources.getString(R.string.capo, getLeaderName())
        }

        descriptionTextView.text = family.info
        Linkify.addLinks(descriptionTextView, Linkify.WEB_URLS)

    }
}