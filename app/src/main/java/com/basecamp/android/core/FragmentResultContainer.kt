package com.basecamp.android.core

import android.graphics.Color
import android.os.Bundle
import android.view.View
import cc.popkorn.inject
import com.basecamp.android.Constants
import com.basecamp.android.shared.extensions.isAssignableFrom
import com.phelat.navigationresult.FragmentResultActivity
import kotlin.reflect.KClass

abstract class FragmentResultContainer<P : Presenter<*, *>> : FragmentResultActivity(), BaseContract.View, BaseContract.Router {

    private var presenter: P? = null

    abstract fun getLayout(): Int
    abstract fun getPresenter(): KClass<P>
    abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (presenter == null) {
            presenter = getPresenter().inject()
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT

        setContentView(getLayout())
        init()

        val bundle = intent?.extras ?: Bundle()
        intent.data?.path?.also { bundle.putString(Constants.OUTSIDE_PARAM, it) }

        notify { init(bundle) }
        notify { attachView(this@FragmentResultContainer) }
    }


    override fun onResume() {
        super.onResume()
        notify { attachRouter(this@FragmentResultContainer) }
    }

    override fun onPause() {
        notify { detachRouter() }
        super.onPause()
    }

    override fun onDestroy() {
        notify { detachView() }
        notify { destroy() }
        super.onDestroy()
    }

    override fun <A : Action> getAction(clazz: KClass<A>): A {
        return presenter.takeIf { clazz.isAssignableFrom(it as Presenter<*, *>) }
            ?.let { it as A } ?: throw RuntimeException("Action could not be found")
    }


    fun notify(lambda: P.() -> Unit) {
        presenter?.apply(lambda) ?: throw RuntimeException("Presenter must not be null at this point")
    }

}