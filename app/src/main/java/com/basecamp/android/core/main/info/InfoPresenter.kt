package com.basecamp.android.core.main.info

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter

@Injectable(Scope.BY_NEW)
class InfoPresenter : Presenter<InfoContract.View, InfoContract.Router>(), InfoContract.Presenter {

    override fun getPageName(): String = "Info"

    override fun init(bundle: Bundle) {}

}