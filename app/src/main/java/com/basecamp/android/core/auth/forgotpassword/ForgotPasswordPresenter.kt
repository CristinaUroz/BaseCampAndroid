package com.basecamp.android.core.auth.forgotpassword

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.data.datasources.ResponseState
import com.basecamp.android.domain.usecases.ForgotPassowrdUseCase
import kotlinx.coroutines.*

@Injectable(Scope.BY_NEW)
class ForgotPasswordPresenter (private val forgotPassowrdUseCase: ForgotPassowrdUseCase): Presenter<ForgotPasswordContract.View, ForgotPasswordContract.Router>(), ForgotPasswordContract.Presenter {

    private val job = SupervisorJob()
    private val errorHandler = CoroutineExceptionHandler { _, _ -> }
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main + errorHandler)


    //val coroutineContext: CoroutineContext = SupervisorJoib() + errorHandler + Dispatchers.Main
    //Implementar CoroutineScope
//ContentLoadingProgressBar

    override fun getPageName(): String = "ForgotPassword"

    override fun init(bundle: Bundle) {}

    override fun onForgotPasswordClick(email: String){
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                when (val response = forgotPassowrdUseCase.forgotPassword(email)) {
                    is ResponseState.Success -> draw { onEmailSent(email) }
                    is ResponseState.Failure -> draw { setError(response.ex.localizedMessage) }
                    else -> draw { setError() }
                }
            }
        }
    }

}