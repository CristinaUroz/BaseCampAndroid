package com.basecamp.android.core.main.info.user

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.User

@Injectable(Scope.BY_NEW)
class UserPresenter(
    private val settingsPreferences: SettingsPreferences
) : Presenter<UserContract.View, UserContract.Router>(), UserContract.Presenter {

    override fun getPageName(): String = "Families"

    override fun init(bundle: Bundle) {}

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(false) }
    }

    override fun onUserRecieved(user: User){
        draw { showError(false) }
        draw { showProgressBar(false) }
        user.apply {
            if (!settingsPreferences.getDarkMode()) {
                name?.let { draw { setName(it) } }
                description?.let { draw { setDescription(it) } }
            } else {
                alias?.let { draw { setName(it) } }
                group?.let { draw { setGroup(it) } }
                descriptionM?.let { draw { setDescription(it) } }
            }
        }
    }

}