package com.basecamp.android.core.main.feed.preview

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import com.basecamp.android.core.main.actions.ShowChangeToDarkMode

@Injectable(Scope.BY_NEW)
class NewsPreviewPresenter() : Presenter<NewsPreviewContract.View, NewsPreviewContract.Router>(), NewsPreviewContract.Presenter {


    override fun getPageName(): String  = "News Preview"

    override fun init(bundle: Bundle) {}

    override fun onResume() {
        delegate(ShowChangeToDarkMode::class) { showChangeToDarkMode(false) }
        super.onResume()
    }

}