package com.basecamp.android.core.main.info

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.GoToWelcomeContainerAction
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.Info
import com.basecamp.android.domain.usecases.GetInfoUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class InfoPresenter(
    private val settingsPreferences: SettingsPreferences,
    private val getInfoUseCase: GetInfoUseCase
) : Presenter<InfoContract.View, InfoContract.Router>(), InfoContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)
    private val isDarkMode by lazy { settingsPreferences.getDarkMode() }

    override fun getPageName(): String = "Info"

    override fun init(bundle: Bundle) {
        draw { setDarkMode(isDarkMode) }
        if (settingsPreferences.getCanWrite()) {
            draw { showCanWrite() }
        }
        getInfo()
    }

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(true) }
    }

    override fun onEnterDarkModeClick() {
        delegate(GoToWelcomeContainerAction::class) { goToWelcomeContainer(Bundle()) }
    }

    private fun getInfo() {
        coroutineScope.launch {
            var info: List<Info>? = emptyList()
            if (isDarkMode) {
                withContext(Dispatchers.IO) {
                    val response = getInfoUseCase.getMafiaInfo()
                    if (response is ResponseState.Success) {
                        info = response.result
                    } else {
                        info = null
                    }
                }
            } else {
                withContext(Dispatchers.IO) {
                    val response = getInfoUseCase.getNormalInfo()
                    info = if (response is ResponseState.Success) {
                        response.result
                    } else {
                        null
                    }
                }
            }
            info?.let { info_list ->
                info_list.takeIf { info_list.isNotEmpty() }?.let {
                    draw { setInformation(it) }
                } ?: draw { setEmpty() }
            } ?: draw { setError() }
        }
    }

}