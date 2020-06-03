package com.basecamp.android.core.main.profile.edit

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.User
import com.basecamp.android.domain.usecases.GetUsersUseCase
import com.basecamp.android.domain.usecases.UpdateUserUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class EditProfilePresenter(
    private val getUsersUseCase: GetUsersUseCase,
    private val settingsPreferences: SettingsPreferences,
    private val updateUserUseCase: UpdateUserUseCase
) : Presenter<EditProfileContract.View, EditProfileContract.Router>(), EditProfileContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)

    private var user: User? = null

    override fun getPageName(): String = "Edit Profile"

    override fun init(bundle: Bundle) {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(false) }

        settingsPreferences.getEmail()?.let {
            coroutineScope.launch {
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

    private fun setUser(user: User) {
        draw { showError(false) }
        draw { showProgressBar(false) }
        user.apply {
            if (!settingsPreferences.getDarkMode()) {
                draw { setInformation(user.image, user.name, user.description) }

            } else {
                draw { setInformation(user.imageM, user.alias, user.descriptionM) }
            }
        }
    }


    override fun onSaveClick(picture: String?, name: String, description: String?) {
        user?.let {
            if (!settingsPreferences.getDarkMode()) {
                it.image = picture
                it.name = name
                it.description = description

            } else {
                it.imageM = picture
                it.alias = name
                it.descriptionM = description
            }

            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    val response = updateUserUseCase.updateUser(it)
                    if (response is ResponseState.Success) {
                        navigate { closeDialog() }
                    }
                }
            }
        }
    }

}