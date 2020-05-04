package com.basecamp.android.core.main

import com.basecamp.android.core.BaseContract

interface MainContract {

    interface View : BaseContract.View

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter
}