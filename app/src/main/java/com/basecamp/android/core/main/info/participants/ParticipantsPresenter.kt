package com.basecamp.android.core.main.info.participants

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.User
import com.basecamp.android.domain.usecases.GetUsersUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class ParticipantsPresenter(
    private val getUsersUseCase: GetUsersUseCase,
    private val settingsPreferences: SettingsPreferences
) : Presenter<ParticipantsContract.View, ParticipantsContract.Router>(), ParticipantsContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)

    override fun getPageName(): String = "Participants"

    override fun init(bundle: Bundle) {
        geUsers()
    }

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(false) }
    }

    private fun geUsers() {
        coroutineScope.launch {
            var users: List<User>? = emptyList()
            withContext(Dispatchers.IO) {
                val response = getUsersUseCase.getAllUsers()
                if (response is ResponseState.Success) {
                    users = response.result
                } else {
                    users = null
                }
            }
            users?.let { info_list ->
                info_list.takeIf { info_list.isNotEmpty() }?.let {
                    if (settingsPreferences.getDarkMode()) {
                        draw { setInformation(it.filter { it.alias != null && it.alias != "" }.sortedBy { it.group }) }
                    } else {
                        draw { setInformation(it.filter { it.name != null && it.name != "" }) }
                    }
                } ?: draw { setEmpty() }
            } ?: draw { setError() }
        }
    }

}