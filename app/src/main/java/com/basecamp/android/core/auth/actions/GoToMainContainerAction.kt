package com.basecamp.android.core.auth.actions

import android.os.Bundle
import cc.popkorn.annotations.Exclude
import com.basecamp.android.core.Action

@Exclude
interface GoToMainContainerAction : Action {
    fun goToMainContainer(bundle: Bundle)
}