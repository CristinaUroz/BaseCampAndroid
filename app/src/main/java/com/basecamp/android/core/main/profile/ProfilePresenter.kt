package com.basecamp.android.core.main.profile

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.GoToSplashContainerAction
import com.basecamp.android.domain.usecases.LogInUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class ProfilePresenter(private val logInUseCase: LogInUseCase) : Presenter<ProfileContract.View, ProfileContract.Router>(), ProfileContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)

    override fun getPageName(): String = "Profile"

    override fun init(bundle: Bundle) {}

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

}