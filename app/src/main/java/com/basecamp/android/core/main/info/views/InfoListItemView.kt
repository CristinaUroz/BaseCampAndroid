package com.basecamp.android.core.main.info.views

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.basecamp.android.R
import com.basecamp.android.core.common.extensions.convertDpToPixel
import com.basecamp.android.domain.models.Info

class InfoListItemView(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    private val editButton by lazy { findViewById<ImageView>(R.id.view_info_item_list_edit) }
    private val title by lazy { findViewById<TextView>(R.id.view_info_item_list_title) }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_info_item_list, this, true)
    }

    fun setInfo(info: Info, canEdit: Boolean, editCallback: (String) -> Unit) {
        editButton.visibility = if (canEdit && info.id != null) View.VISIBLE else View.GONE
        info.id?.let { id -> editButton.setOnClickListener { editCallback.invoke(id) } }
        info.title?.let {
            title.text = it
        } ?: title.apply { visibility = View.GONE }

        title.setOnClickListener {
            AlertDialog.Builder(context)
                .setCustomTitle(TextView(context).apply {
                    text = info.title
                    setTextIsSelectable(true)
                    textSize = 24f
                    context?.apply {
                        setPadding(convertDpToPixel(12f).toInt(), convertDpToPixel(24f).toInt(), convertDpToPixel(12f).toInt(), convertDpToPixel(6f).toInt())
                    }
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    setTextColor(Color.BLACK)
                })
                .setView(TextView(context).apply {
                    text = info.text
                    setTextIsSelectable(true)
                    movementMethod = LinkMovementMethod.getInstance()
                    Linkify.addLinks(this, Linkify.WEB_URLS)
                    context?.apply {
                        setPadding(convertDpToPixel(12f).toInt(), 0, convertDpToPixel(12f).toInt(), convertDpToPixel(24f).toInt())
                    }
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                })
                .show()
        }
    }


}
