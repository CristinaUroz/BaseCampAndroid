package com.basecamp.android.core.auth

import android.os.Bundle
import com.basecamp.android.core.BaseContract

interface AuthContract {

    interface View : BaseContract.View

    interface Router : BaseContract.Router {
        fun goToMain(bundle: Bundle)
    }

    interface Presenter : BaseContract.Presenter

}