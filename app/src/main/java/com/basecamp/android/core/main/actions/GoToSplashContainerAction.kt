package com.basecamp.android.core.main.actions

import android.os.Bundle
import cc.popkorn.annotations.Exclude
import com.basecamp.android.core.Action

@Exclude
interface GoToSplashContainerAction : Action {
    fun goToSplashContainer(bundle: Bundle)
}