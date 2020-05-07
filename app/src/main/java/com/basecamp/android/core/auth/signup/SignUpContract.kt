package com.basecamp.android.core.auth.signup

import com.basecamp.android.core.BaseContract

interface SignUpContract {

    interface View : BaseContract.View {
        fun setError(error: String? = null)
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter {
        fun onSignUpClick(name: String, email: String, password: String)
    }

}