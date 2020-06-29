package com.basecamp.android.core.main.info.participants

import com.basecamp.android.core.BaseContract
import com.basecamp.android.domain.models.User

interface ParticipantsContract {

    interface View : BaseContract.View {
        fun setInformation(list: List<User>)
        fun setError(lambda: () -> Unit = {})
        fun setEmpty(emptyText: String? = null, emptyButton: String? = null, emptyButtonCallback: () -> Unit = {})
    }

    interface Router : BaseContract.Router

    interface Presenter : BaseContract.Presenter

}