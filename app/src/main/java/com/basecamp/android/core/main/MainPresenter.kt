package com.basecamp.android.core.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.GoToSplashContainerAction
import com.basecamp.android.core.main.actions.GoToWelcomeContainerAction
import com.basecamp.android.data.repositories.datasources.SettingsPreferences

@Injectable(Scope.BY_NEW)
class MainPresenter(private val settingsPreferences: SettingsPreferences) : Presenter<MainContract.View, MainContract.Router>(), MainContract.Presenter, GoToWelcomeContainerAction, GoToSplashContainerAction {

    override fun getPageName(): String = "Main"

    override fun init(bundle: Bundle) {
        draw { showFloatingButton(settingsPreferences.getCanEnableDarkMode()) }
    }

    override fun onDarkModeClick() {
        if (settingsPreferences.getDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            settingsPreferences.setDarkMode(false)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            settingsPreferences.setDarkMode(true)
        }
    }

    override fun goToWelcomeContainer(bundle: Bundle) {
        navigate { goToWelcome(bundle) }
    }

    override fun goToSplashContainer(bundle: Bundle) {
        navigate { goToSplash(bundle) }
    }
}