package com.basecamp.android.core.main.info

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.GoToWelcomeContainerAction
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.repositories.datasources.SettingsPreferences

@Injectable(Scope.BY_NEW)
class InfoPresenter(private val settingsPreferences: SettingsPreferences) : Presenter<InfoContract.View, InfoContract.Router>(), InfoContract.Presenter {

    override fun getPageName(): String = "Info"

    override fun init(bundle: Bundle) {}

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class){showChangeToDarkMode(true)}
    }
    override fun onEnterDarkModeClick() {
        delegate(GoToWelcomeContainerAction::class){goToWelcomeContainer(Bundle())}
    }

}