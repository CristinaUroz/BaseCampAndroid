package com.basecamp.android.core.auth.signup

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.auth.actions.GoToMainContainerAction
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.usecases.SignUpUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class SignUpPresenter(private val signUpUseCase: SignUpUseCase) : Presenter<SignUpContract.View, SignUpContract.Router>(), SignUpContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)

    override fun getPageName(): String = "SignUp"

    override fun init(bundle: Bundle) {}

    override fun onSignUpClick(name: String, email: String, password: String) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                when (val response = signUpUseCase.signUp(name, email, password)) {
                    is ResponseState.Success -> goToMainContainer(Bundle())
                    is ResponseState.Failure -> draw { setError(response.ex.localizedMessage) }
                    else -> draw { setError() }
                }
            }
        }

    }

    private fun goToMainContainer(bundle: Bundle) {
        delegate(GoToMainContainerAction::class) { goToMainContainer(bundle) }
    }

}