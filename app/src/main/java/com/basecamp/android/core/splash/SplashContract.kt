package com.basecamp.android.core.splash

import android.os.Bundle
import com.basecamp.android.core.BaseContract

interface SplashContract {

    interface View : BaseContract.View

    interface Router : BaseContract.Router {
        fun goToMain(bundle: Bundle)
        fun goToAuth(bundle: Bundle)
    }

    interface Presenter : BaseContract.Presenter
}