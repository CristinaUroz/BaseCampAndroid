package com.basecamp.android.core.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import cc.popkorn.annotations.Injectable
import cc.popkorn.core.Scope
import com.basecamp.android.core.Presenter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Injectable(Scope.BY_NEW)
class SplashPresenter : Presenter<SplashContract.View, SplashContract.Router>(), SplashContract.Presenter {
    override fun getPageName(): String = "Splash"

    override fun init(bundle: Bundle) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        GlobalScope.launch {
            delay(1500)
            navigate { goToAuth(Bundle()) }
        }
    }

}