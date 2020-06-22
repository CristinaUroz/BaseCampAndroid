package com.basecamp.android.core.main.feed

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.basecamp.android.R
import com.basecamp.android.core.common.extensions.BCGlide
import com.basecamp.android.core.common.extensions.convertTimestampToDate
import com.basecamp.android.core.common.extensions.getGroupLogo
import com.basecamp.android.domain.models.News

class NewsListItemView(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    private val editButton by lazy { findViewById<ImageView>(R.id.view_news_item_list_edit) }
    private val userPicture by lazy { findViewById<ImageView>(R.id.view_news_item_list_user_picture) }
    private val picture by lazy { findViewById<ImageView>(R.id.view_news_item_list_picture) }
    private val title by lazy { findViewById<TextView>(R.id.view_news_item_list_title) }
    private val date by lazy { findViewById<TextView>(R.id.view_news_item_list_date) }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_news_item_list, this, true)
    }

    fun setInfo(news: News, canEdit: Boolean, editCallback: (String) -> Unit) {
        editButton.visibility = if (canEdit && news.id != null) View.VISIBLE else View.GONE
        news.id?.let { id -> editButton.setOnClickListener { editCallback.invoke(id) } }
        userPicture.visibility = if (news.mafia && news.author != null) View.VISIBLE else View.GONE
        date.text = news.timestamp?.let{ convertTimestampToDate(it) } ?: ""
        news.author?.let {
            userPicture.setImageResource(it.getGroupLogo())
        }
        (news.picture.takeIf { it!="" } ?: context.getDrawable(R.drawable.newspapers))?.let {
            BCGlide(context)
                .load(it)
                .centerCrop()
                .into(picture)
        } ?: picture.apply { visibility = View.GONE }
        news.title?.let {
            title.text = it
        } ?: title.apply { visibility = View.GONE }
    }

}
