package com.basecamp.android.core.main.feed

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.main.feed.adapters.NewsRecyclerViewAdapter
import com.basecamp.android.domain.models.News
import kotlin.reflect.KClass

class FeedScreen : Screen<FeedPresenter>(), FeedContract.View, FeedContract.Router {

    private val newsRecyclerViewAdapter = NewsRecyclerViewAdapter()
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.screen_feed_list_recycler_view) }
    private val frameContainer by lazy { findViewById<RelativeLayout>(R.id.screen_feed_list_frame_container) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.screen_feed_list_progress_bar) }
    private val swipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.screen_feed_list_swipe_refresh_layout) }
    private val addButton by lazy { findViewById<ImageView>(R.id.screen_feed_add_button) }
    private var whenScroll = {}


    override fun getLayout(): Int = R.layout.screen_feed

    override fun getPresenter(): KClass<FeedPresenter> = FeedPresenter::class

    override fun init() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsRecyclerViewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    whenScroll.invoke()
                }
            })
        }
        newsRecyclerViewAdapter.apply {
            setOnEditClickListener {
                navigate(FeedScreenDirections.actionFeedScreenToAddnewScreen(it))
            }
            setOnClickListener {
                navigate(FeedScreenDirections.actionFeedScreenToNewspreviewScreen(it))
            }
        }
        swipeRefreshLayout.isEnabled = false
        context?.let { swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(it, R.color.colorPrimaryDark)) }
        swipeRefreshLayout.setSize(48)
        swipeRefreshLayout.isEnabled = true
        addButton.setOnClickListener {
            navigate(FeedScreenDirections.actionFeedScreenToAddnewScreen())
        }
    }

    override fun showCanWrite() {
        addButton.visibility = View.VISIBLE
    }

    override fun setInformation(list: List<News>) {
        newsRecyclerViewAdapter.setData(list.sortedByDescending { it.timestamp })
        recyclerView.visibility = View.VISIBLE
        setProgressDialog(false)
        if (list.isNotEmpty()) {
            frameContainer.removeAllViews()
            frameContainer.visibility = View.GONE
        }
    }

    override fun setOnItemClickListener(OnClickListener: (News) -> Unit) {
        newsRecyclerViewAdapter.setOnClickListener {
            OnClickListener.invoke(it)
        }
    }

    override fun setOnRefreshingCallback(refreshingCallback: () -> Unit) {
        swipeRefreshLayout.setOnRefreshListener { refreshingCallback.invoke() }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun setRefreshing(refreshing: Boolean){
        swipeRefreshLayout.isRefreshing = refreshing
    }

    fun getFirstVisibleItemPosition(): Int {
        return if (recyclerView.visibility == FrameLayout.GONE || recyclerView.size == 0) 0
        else (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    override fun setError(lambda: () -> Unit) {
        progressBar.visibility = View.GONE
        frameContainer.removeAllViews()
        frameContainer.visibility = View.VISIBLE
        //TODO frameContainer.addView(ErrorImageView(context, lambda).apply { id = R.id.routes_list_view_error_screen })
    }

    private fun setProgressDialog(b: Boolean) {
        progressBar.visibility = if (b) {
            frameContainer.removeAllViews()
            frameContainer.visibility = View.GONE
            View.VISIBLE
        } else View.GONE
    }

    override fun setEmpty(emptyText: String?, emptyButton: String?, emptyButtonCallback: () -> Unit) {
        progressBar.visibility = View.GONE
        frameContainer.removeAllViews()
        frameContainer.visibility = View.VISIBLE
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        //TODO frameContainer.addView(EmptyScreenImageView(context,emptyText, emptyButton, emptyButtonCallback).apply { id = R.id.routes_list_view_empty_screen }, layoutParams)
    }

}