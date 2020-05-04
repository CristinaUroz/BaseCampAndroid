package com.basecamp.android.core.auth.login

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter

@Injectable(Scope.BY_NEW)
class LogInPresenter : Presenter<LogInContract.View, LogInContract.Router>(), LogInContract.Presenter {

    override fun getPageName(): String = "LogIn"

    override fun init(bundle: Bundle) {}

}