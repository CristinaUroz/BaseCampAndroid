package com.basecamp.android.core.auth.forgotpassword

import com.basecamp.android.core.BaseContract

interface ForgotPasswordContract {

    interface View : BaseContract.View {
        fun setError(error: String? = null)
        fun onEmailSent(email: String)
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter {
        fun onForgotPasswordClick(email: String)
    }

}