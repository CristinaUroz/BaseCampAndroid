package com.basecamp.android.core.auth.signup

import android.os.Bundle
import com.basecamp.android.core.BaseContract

interface SignUpContract {

    interface View : BaseContract.View

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter {
        fun goToMainContainer(bundle: Bundle)
    }

}