package com.basecamp.android.core.welcome

import android.os.Bundle
import com.basecamp.android.core.BaseContract
import com.basecamp.android.domain.models.MafiaWelcome

interface WelcomeContract {

    interface View : BaseContract.View {
        fun setWelcome(welcome: MafiaWelcome)
        fun setError()
    }

    interface Router : BaseContract.Router {
        fun goToMain(bundle: Bundle)
    }

    interface Presenter : BaseContract.Presenter

}