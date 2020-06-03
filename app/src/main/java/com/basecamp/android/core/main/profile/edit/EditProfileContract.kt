package com.basecamp.android.core.main.profile.edit

import com.basecamp.android.core.BaseContract

interface EditProfileContract {

    interface View : BaseContract.View {
        fun setInformation(picture: String?, name: String?, description: String?)
        fun showProgressBar(b: Boolean)
        fun showError(b: Boolean)
    }

    interface Router : BaseContract.Router{
        fun closeDialog()
    }

    interface Presenter : BaseContract.Presenter {
        fun onSaveClick(picture: String?, name: String, description: String?)
    }

}