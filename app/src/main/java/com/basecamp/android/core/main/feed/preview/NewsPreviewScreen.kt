package com.basecamp.android.core.main.feed.preview

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.common.extensions.BCGlide
import com.basecamp.android.core.common.extensions.convertTimestampToDate
import com.basecamp.android.core.common.extensions.getGroupLogo
import com.basecamp.android.core.common.extensions.getLeaderName
import com.basecamp.android.domain.models.News
import kotlin.reflect.KClass

class NewsPreviewScreen : Screen<NewsPreviewPresenter>(), NewsPreviewContract.View, NewsPreviewContract.Router {

    private val data: NewsPreviewScreenArgs by navArgs()
    private val title by lazy { findViewById<TextView>(R.id.screen_newspreview_title) }
    private val authorLayout by lazy { findViewById<LinearLayout>(R.id.screen_newspreview_author) }
    private val authorPicture by lazy { findViewById<ImageView>(R.id.screen_newspreview_author_picture) }
    private val authorName by lazy { findViewById<TextView>(R.id.screen_newspreview_author_name) }
    private val text by lazy { findViewById<TextView>(R.id.screen_newspreview_text) }
    private val date by lazy { findViewById<TextView>(R.id.screen_newspreview_date) }
    private val picture by lazy { findViewById<ImageView>(R.id.screen_newspreview_cover_picture) }

    override fun getLayout(): Int = R.layout.screen_newspreview

    override fun getPresenter(): KClass<NewsPreviewPresenter> = NewsPreviewPresenter::class

    override fun init() {
        ///android:textIsSelectable="true"
        text.movementMethod = LinkMovementMethod.getInstance()
        setNews(data.news)
    }

    private fun setNews(news: News) {
        title.text = news.title
        text.text = news.text
        date.text = news.timestamp.let{convertTimestampToDate(it)}
        Linkify.addLinks(text, Linkify.WEB_URLS)
        if (news.mafia && news.author != null) {
            authorLayout.visibility = View.VISIBLE
            news.author?.apply {
                authorName.text = getLeaderName()
                authorPicture.setImageResource(getGroupLogo())
            }
        } else {
            authorLayout.visibility = View.GONE
        }
        context?.let { ctx ->
            news.picture?.let {
                BCGlide(ctx)
                    .load(it)
                    .into(picture)
            }
        }
    }
}