package com.basecamp.android.core.main.actions

import cc.popkorn.annotations.Exclude
import com.basecamp.android.core.Action

@Exclude
interface ShowChangeToDarkMode : Action {
    fun showChangeToDarkMode(b: Boolean)
}