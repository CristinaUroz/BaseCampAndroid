package com.basecamp.android.core.main.feed.add

import com.basecamp.android.core.BaseContract
import com.basecamp.android.domain.models.News

interface AddNewContract {

    interface View : BaseContract.View{
        fun darkVersion(b: Boolean)
        fun setInformation(news: News)
        fun showError(b: Boolean)
        fun showProgressBar(b: Boolean)
        fun setError(error: String)
    }

    interface Router : BaseContract.Router {
        fun closeDialog()
    }

    interface Presenter : BaseContract.Presenter {
        fun getNews(id: String)
        fun onSaveClick(picture: String?, title: String, text: String, author: Int?, timeStamp: Long)
        fun onDeleteClick()
    }

}