package com.basecamp.android.core.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.data.repositories.datasources.SettingsPreferences
import com.basecamp.android.domain.usecases.CurrentUserUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class SplashPresenter(private val settingsPreferences: SettingsPreferences, private val currentUserUseCase: CurrentUserUseCase) : Presenter<SplashContract.View, SplashContract.Router>(), SplashContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)

    override fun getPageName(): String = "Splash"


    private fun startNavigation() = coroutineScope.launch {
        withContext(Dispatchers.IO) {
            val email = CoroutineScope(Dispatchers.IO).async { currentUserUseCase.getCurrentUserMail() }
            val delay = CoroutineScope(Dispatchers.IO).async { delay(1500) }

            delay.await().also {
                email.await()?.let {
                    navigate { goToMain(Bundle()) }
                } ?: navigate { goToAuth(Bundle()) }
            }
        }

    }

    override fun init(bundle: Bundle) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        settingsPreferences.setDarkMode(false)
        startNavigation()
    }

}