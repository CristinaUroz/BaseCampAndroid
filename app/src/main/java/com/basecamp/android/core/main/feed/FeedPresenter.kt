package com.basecamp.android.core.main.feed

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter

@Injectable(Scope.BY_NEW)
class FeedPresenter : Presenter<FeedContract.View, FeedContract.Router>(), FeedContract.Presenter {

    override fun getPageName(): String = "Feed"

    override fun init(bundle: Bundle) {}

}