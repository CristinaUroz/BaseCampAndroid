package com.basecamp.android.core.main.info.add

import com.basecamp.android.core.BaseContract
import com.basecamp.android.domain.models.Info

interface AddInfoContract {

    interface View : BaseContract.View{
        fun setInformation(info: Info)
        fun showError(b: Boolean)
        fun showProgressBar(b: Boolean)
        fun setError(error: String)
    }

    interface Router : BaseContract.Router {
        fun closeDialog()
    }

    interface Presenter : BaseContract.Presenter {
        fun getInfo(id: String)
        fun onSaveClick(title: String, text: String)
        fun onDeleteClick()
    }

}