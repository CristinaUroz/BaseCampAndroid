package com.basecamp.android.core

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cc.popkorn.inject
import com.basecamp.android.Constants
import com.basecamp.android.shared.extensions.isAssignableFrom
import kotlin.reflect.KClass

abstract class Container<P : Presenter<*, *>> : AppCompatActivity(), BaseContract.View, BaseContract.Router {

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
        notify { attachView(this@Container) }
    }


    override fun onResume() {
        super.onResume()
        notify { attachRouter(this@Container) }
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

    open fun onBackPressed2(): Boolean = false


    override fun <A : Action> getAction(clazz: KClass<A>): A {
        return presenter.takeIf { clazz.isAssignableFrom(it as Presenter<*, *>) }
            ?.let { it as A } ?: throw RuntimeException("Action could not be found")
    }

    fun notify(lambda: P.() -> Unit) {
        presenter?.apply(lambda) ?: throw RuntimeException("Presenter must not be null at this point")
    }

}