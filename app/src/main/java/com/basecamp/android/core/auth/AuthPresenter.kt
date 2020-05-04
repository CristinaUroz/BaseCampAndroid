package com.basecamp.android.core.auth

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.auth.actions.GoToMainContainerAction

@Injectable(Scope.BY_NEW)
class AuthPresenter : Presenter<AuthContract.View, AuthContract.Router>(), AuthContract.Presenter, GoToMainContainerAction {

    override fun getPageName(): String = "Auth"

    override fun init(bundle: Bundle) {}

    override fun goToMainContainer(bundle: Bundle) {
        navigate { goToMain(bundle) }
    }

}