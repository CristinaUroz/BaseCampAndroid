package com.basecamp.android.core.common.cameragallery

import android.os.Bundle
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter

@Injectable(Scope.BY_NEW)
class CameraGalleryPresenter: Presenter<CameraGalleryContract.View, CameraGalleryContract.Router>(), CameraGalleryContract.Presenter {

    override fun init(bundle: Bundle){}

    override fun getPageName(): String = "CameraGallery"

}