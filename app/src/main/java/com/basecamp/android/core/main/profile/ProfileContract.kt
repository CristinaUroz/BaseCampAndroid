package com.basecamp.android.core.main.profile

import com.basecamp.android.core.BaseContract

interface ProfileContract {

    interface View : BaseContract.View {
        fun setName(name: String)
        fun setDescription (description: String)
        fun setEmail (email:String)
        fun setGroup (group: Int)
        fun setPicture (picture: String?)
        fun showError (b: Boolean)
        fun showProgressBar(b: Boolean)
        fun showCanEdit(b: Boolean)
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter {
        fun onLogOutClick()
    }
}