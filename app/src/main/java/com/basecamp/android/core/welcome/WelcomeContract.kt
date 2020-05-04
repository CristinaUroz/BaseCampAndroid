package com.basecamp.android.core.welcome

import android.os.Bundle
import com.basecamp.android.core.BaseContract

interface WelcomeContract {

    interface View : BaseContract.View

    interface Router : BaseContract.Router {
        fun goToMain(bundle: Bundle)
    }

    interface Presenter : BaseContract.Presenter

}