package com.basecamp.android.core.main.info.user

import com.basecamp.android.core.BaseContract
import com.basecamp.android.domain.models.User

interface UserContract {

    interface View : BaseContract.View {
        fun setGroup(group: Int)
        fun setDescription(description: String)
        fun setName(name: String)
        fun showProgressBar(b: Boolean)
        fun showError(b: Boolean)
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter {
        fun onUserRecieved(user: User)
    }

}