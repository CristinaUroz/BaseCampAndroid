package com.basecamp.android.core.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.data.repositories.datasources.SettingsPreferences

@Injectable(Scope.BY_NEW)
class WelcomePresenter(private val settingsPreferences: SettingsPreferences) : Presenter<WelcomeContract.View, WelcomeContract.Router>(), WelcomeContract.Presenter {

    override fun getPageName(): String = "Welcome"

    override fun init(bundle: Bundle) {
        settingsPreferences.setCanEnableDarkMode(true)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        settingsPreferences.setDarkMode(true)
    }
}