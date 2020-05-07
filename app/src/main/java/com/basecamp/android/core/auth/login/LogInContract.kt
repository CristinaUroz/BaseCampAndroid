package com.basecamp.android.core.auth.login

import com.basecamp.android.core.BaseContract

interface LogInContract {

    interface View : BaseContract.View {
        fun setError(error: String? = null)
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter{
        fun onLogInClick(email: String, password: String)
    }

}