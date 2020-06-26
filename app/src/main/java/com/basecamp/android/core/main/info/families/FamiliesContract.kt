package com.basecamp.android.core.main.info.families

import com.basecamp.android.core.BaseContract
import com.basecamp.android.domain.models.Family

interface FamiliesContract {

    interface View : BaseContract.View{
        fun setError(lambda: () -> Unit = {})
        fun setEmpty(emptyText: String? = null, emptyButton: String? = null, emptyButtonCallback: () -> Unit = {})
        fun setInformation(list: List<Family>)
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter

}