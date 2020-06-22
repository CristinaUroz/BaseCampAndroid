package com.basecamp.android.core.main.profile

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.GoToSplashContainerAction
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.User
import com.basecamp.android.domain.usecases.GetUsersUseCase
import com.basecamp.android.domain.usecases.LogInUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class ProfilePresenter(
    private val logInUseCase: LogInUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val settingsPreferences: SettingsPreferences
) : Presenter<ProfileContract.View, ProfileContract.Router>(), ProfileContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)

    override fun getPageName(): String = "Profile"

    override fun init(bundle: Bundle) {}

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(true) }
        settingsPreferences.getEmail()?.let {
            coroutineScope.launch {
                var user: User? = null
                draw { showProgressBar(true) }
                withContext(Dispatchers.IO) {
                    val response = getUsersUseCase.getUser(it)
                    if (response is ResponseState.Success) {
                        user = response.result

                    }
                }
                user?.let { setUser(it) } ?: draw { showError(true) }
            }
        } ?: draw { showError(true) }
    }

    override fun onLogOutClick() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                logInUseCase.logOut()
                goToSplashContainer(Bundle())
            }
        }
    }

    private fun goToSplashContainer(bundle: Bundle) {
        delegate(GoToSplashContainerAction::class) { goToSplashContainer(bundle) }
    }

    private fun setUser(user: User) {
        settingsPreferences.setCanWrite(user.adult ?: false)
        draw { showError(false) }
        draw { showProgressBar(false) }
        user.apply {
            draw { showCanEdit(true) }
            if (!settingsPreferences.getDarkMode()) {
                name?.let { draw { setName(it) } }
                email?.let { draw { setEmail(it) } }
                description?.let { draw { setDescription(it) } }
                draw { setPicture(image) }
            } else {
                alias?.let { draw { setName(it) } }
                group?.let { draw { setGroup(it) } }
                descriptionM?.let { draw { setDescription(it) } }
                draw { setPicture(imageM) }
            }
        }
    }

}