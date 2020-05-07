package com.basecamp.android.core.main.profile

import com.basecamp.android.core.BaseContract

interface ProfileContract {

    interface View : BaseContract.View

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter {
        fun onLogOutClick()
    }
}