package com.basecamp.android.core.main.info

import android.widget.Button
import com.basecamp.android.R
import com.basecamp.android.core.Screen
import com.basecamp.android.core.main.info.common.MultiTapDetector
import kotlin.reflect.KClass


class InfoScreen : Screen<InfoPresenter>(), InfoContract.View, InfoContract.Router {

    private val screenInfo by lazy { findViewById<Button>(R.id.screen_info_version_number) }

    override fun getLayout(): Int = R.layout.screen_info

    override fun getPresenter(): KClass<InfoPresenter> = InfoPresenter::class

    override fun init() {

        MultiTapDetector(screenInfo) {i, _ ->
            if (i >= 5) {
                notify { onEnterDarkModeClick() }
            }
        }
    }

}