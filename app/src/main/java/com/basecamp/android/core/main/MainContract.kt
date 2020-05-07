package com.basecamp.android.core.main

import android.os.Bundle
import com.basecamp.android.core.BaseContract

interface MainContract {

    interface View : BaseContract.View {
        fun showFloatingButton(b: Boolean)
    }

    interface Router : BaseContract.Router {
        fun goToWelcome(bundle: Bundle)
        fun goToSplash(bundle: Bundle)
    }

    interface Presenter : BaseContract.Presenter {
        fun onDarkModeClick()
    }
}