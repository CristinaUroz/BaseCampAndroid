package com.basecamp.android.core.main.info

import com.basecamp.android.core.BaseContract
import com.basecamp.android.domain.models.Info

interface InfoContract {

    interface View : BaseContract.View {
        fun showCanWrite()
        fun setDarkMode(b: Boolean)
        fun setInformation(list: List<Info>)
        fun setError(lambda: () -> Unit = {})
        fun setEmpty(emptyText: String? = null, emptyButton: String? = null, emptyButtonCallback: () -> Unit = {})
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter {
        fun onEnterDarkModeClick()
    }

}