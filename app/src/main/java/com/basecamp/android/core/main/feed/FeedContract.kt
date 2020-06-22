package com.basecamp.android.core.main.feed

import com.basecamp.android.core.BaseContract
import com.basecamp.android.domain.models.News

interface FeedContract {

    interface View : BaseContract.View {
        fun showCanWrite()
        fun setInformation(list: List<News>)
        fun setError(lambda: () -> Unit = {})
        fun setEmpty(emptyText: String? = null, emptyButton: String? = null, emptyButtonCallback: () -> Unit = {})
        fun setOnRefreshingCallback(refreshingCallback: () -> Unit)
        fun setOnItemClickListener(OnClickListener: (News) -> Unit = {})
        fun setRefreshing(refreshing: Boolean)
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter

}