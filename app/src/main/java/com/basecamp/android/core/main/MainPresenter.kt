package com.basecamp.android.core.main

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter

@Injectable(Scope.BY_NEW)
class MainPresenter : Presenter<MainContract.View, MainContract.Router>(), MainContract.Presenter {

    override fun getPageName(): String = "Main"

    override fun init(bundle: Bundle) {}

}