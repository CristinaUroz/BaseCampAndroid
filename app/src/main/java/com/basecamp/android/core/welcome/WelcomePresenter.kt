package com.basecamp.android.core.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.models.MafiaWelcome
import com.basecamp.android.domain.usecases.GetMafiaWelcomeUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class WelcomePresenter(
    private val settingsPreferences: SettingsPreferences,
    private val welcomeUseCase: GetMafiaWelcomeUseCase
) : Presenter<WelcomeContract.View, WelcomeContract.Router>(), WelcomeContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)

    override fun getPageName(): String = "Welcome"

    override fun init(bundle: Bundle) {
        settingsPreferences.setCanEnableDarkMode(true)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        settingsPreferences.setDarkMode(true)

        coroutineScope.launch {
            var welcome: MafiaWelcome? = null
            withContext(Dispatchers.IO) {
                val response = welcomeUseCase.getMafiaWelcome()
                if (response is ResponseState.Success) {
                    welcome = response.result
                } else {
                    welcome = null
                }
            }
            welcome?.let {
                draw { setWelcome(it) }
            } ?: draw { setError() }
        }
    }
}