package com.basecamp.android.core.main.profile

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter

@Injectable(Scope.BY_NEW)
class ProfilePresenter : Presenter<ProfileContract.View, ProfileContract.Router>(), ProfileContract.Presenter {

    override fun getPageName(): String = "Profile"

    override fun init(bundle: Bundle) {}

}