package com.basecamp.android.core

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cc.popkorn.inject
import com.basecamp.android.Constants
import com.basecamp.android.R
import com.basecamp.android.shared.extensions.isAssignableFrom
import com.basecamp.android.shared.extensions.newInstance
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

    override fun onBackPressed() {
        supportFragmentManager.fragments.lastOrNull()
            ?.let { (it as? Screen<*>) }
            ?.takeIf { it.onBackPressed() }
            ?: takeIf { onBackPressed2() }
            ?: super.onBackPressed()
    }

    open fun onBackPressed2(): Boolean = false

    override fun close(bundle: Bundle) {
        close()
    }

    override fun close() {
        finish()
    }

    override fun <A : Action> getAction(clazz: KClass<A>): A {
        return presenter.takeIf { clazz.isAssignableFrom(it as Presenter<*, *>) }
            ?.let { it as A } ?: throw RuntimeException("Action could not be found")
    }

    @Synchronized
    fun set(clazz: KClass<out Screen<*>>, bundle: Bundle) {
        for (x in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
        val screen = clazz.newInstance()
        screen.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_container_content, screen, clazz.simpleName).commit()
    }

    @Synchronized
    fun start(clazz: KClass<out Screen<*>>, bundle: Bundle) {
        val screen = clazz.newInstance()
        screen.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_container_content, screen, clazz.simpleName).addToBackStack(clazz.simpleName).commit()
    }

    @Synchronized
    fun startDialog(clazz: KClass<out Screen<*>>, bundle: Bundle, callback: (Bundle) -> Unit = {}) {
        val screen = clazz.newInstance()
        screen.arguments = bundle
        screen.result = callback
        supportFragmentManager.beginTransaction().add(R.id.main_container_content, screen, clazz.simpleName).addToBackStack(clazz.simpleName).commit()
    }

    fun notify(lambda: P.() -> Unit) {
        presenter?.apply(lambda) ?: throw RuntimeException("Presenter must not be null at this point")
    }

}