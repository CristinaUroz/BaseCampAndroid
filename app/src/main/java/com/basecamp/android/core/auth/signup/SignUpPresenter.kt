package com.basecamp.android.core.auth.signup

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.auth.actions.GoToMainContainerAction

@Injectable(Scope.BY_NEW)
class SignUpPresenter : Presenter<SignUpContract.View, SignUpContract.Router>(), SignUpContract.Presenter {

    override fun getPageName(): String = "SignUp"

    override fun init(bundle: Bundle) {}

    override fun goToMainContainer(bundle: Bundle) {
        delegate(GoToMainContainerAction::class){ goToMainContainer(bundle)}
    }
}