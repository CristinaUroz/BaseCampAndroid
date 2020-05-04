package com.basecamp.android.core.auth.forgotpassword

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter

@Injectable(Scope.BY_NEW)
class ForgotPasswordPresenter : Presenter<ForgotPasswordContract.View, ForgotPasswordContract.Router>(), ForgotPasswordContract.Presenter {

    override fun getPageName(): String = "ForgotPassword"

    override fun init(bundle: Bundle) {}

}