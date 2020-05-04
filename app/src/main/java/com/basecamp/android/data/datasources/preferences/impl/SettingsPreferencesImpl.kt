package com.basecamp.android.data.datasources.preferences.impl

import android.content.Context
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.data.datasources.preferences.engine.Preferences
import com.basecamp.android.data.repositories.datasources.SettingsPreferences

@Injectable(Scope.BY_APP, exclude = [Preferences::class])
internal class SettingsPreferencesImpl(ctx: Context) : Preferences(ctx, "Settings"), SettingsPreferences {

    companion object {
        const val CAN_ENABLE_DARK_MODE = "CAN_ENABLE_DARK_MODE"
        const val DARK_MODE = "DARK_MODE"
    }

    override fun setCanEnableDarkMode(canEnableDarkMode: Boolean) = putBoolean(CAN_ENABLE_DARK_MODE, canEnableDarkMode)

    override fun getCanEnableDarkMode(): Boolean = getBoolean(CAN_ENABLE_DARK_MODE, false)

    override fun setDarkMode(darkMode: Boolean) = putBoolean(DARK_MODE, darkMode)

    override fun getDarkMode(): Boolean = getBoolean(DARK_MODE, false)


}