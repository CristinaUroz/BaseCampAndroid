package com.basecamp.android.core


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cc.popkorn.inject
import com.basecamp.android.R
import com.basecamp.android.shared.extensions.isAssignableFrom
import com.basecamp.android.shared.extensions.newInstance
import kotlin.reflect.KClass


abstract class Screen<P : Presenter<*, *>> : Fragment(), BaseContract.View, BaseContract.Router {

    var result: ((Bundle) -> Unit)? = null
    private var navigationArgs: Bundle = Bundle()

    private var presenter: P? = null
    private var fullview: View? = null

    abstract fun getLayout(): Int
    abstract fun getPresenter(): KClass<P>
    abstract fun init()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (presenter == null) {
            presenter = getPresenter().inject()
                .apply { init(arguments ?: Bundle()) }
        }

        return if (this.fullview != null) {
            this.fullview
        } else {
            this.fullview = inflater.inflate(getLayout(), container, false)
            init()
            this.fullview
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notify { attachView(this@Screen) }
    }

    override fun onResume() {
        super.onResume()
        notify { attachRouter(this@Screen) }
    }

    override fun onPause() {
        notify { detachRouter() }
        super.onPause()
    }

    override fun onDestroyView() {
        notify { detachView() }
        super.onDestroyView()
    }

    override fun onDestroy() {
        notify { destroy() }
        super.onDestroy()
        result?.invoke(navigationArgs)
    }

    override fun close(bundle: Bundle) {
        navigationArgs = bundle
        close()
    }

    override fun close() {
        //si el manager es null, vol dir que el fragment no esta attachat a res, el close no ha de fer res
        val fragmentManager = fragmentManager ?: return
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            activity?.onBackPressed()
        }
    }

    override fun <A : Action> getAction(clazz: KClass<A>): A {
        val currentPresenter = presenter ?: throw RuntimeException("Presenter is not present")

        if (clazz.isAssignableFrom(currentPresenter)) return currentPresenter as A

        fragmentManager?.fragments
            ?.filterIsInstance<Screen<*>>()
            ?.map { it.presenter }
            ?.filterNotNull()
            ?.filter { clazz.isAssignableFrom(it) }
            ?.map { it as A }
            ?.firstOrNull()
            ?.also { return it }


        return if (parentFragment != null && parentFragment is BaseContract.Router) {
            (parentFragment as BaseContract.Router).getAction(clazz)
        } else if (activity is BaseContract.Router) {
            (activity as BaseContract.Router).getAction(clazz)
        } else {
            throw RuntimeException("Action could not be found")
        }
    }

    fun start(clazz: KClass<out Screen<*>>, params: Bundle, onResult: ((Bundle) -> Unit)? = null) {
        val screen = clazz.newInstance()
            .apply {
                arguments = params
                result = onResult
            }
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_container_content, screen, clazz.simpleName)?.addToBackStack(clazz.simpleName)?.commit()
    }

    fun attach(container: Int, clazz: KClass<out Screen<*>>, params: Bundle) {
        val screen = clazz.newInstance().apply { arguments = params }
        childFragmentManager.beginTransaction().replace(container, screen, clazz.simpleName).commit()
    }

    fun startDialog(clazz: KClass<out Dialog<*>>, params: Bundle, onResult: ((Bundle) -> Unit)? = null) {
        val screen = clazz.newInstance()
            .apply {
                arguments = params
                result = onResult
            }
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.main_container_content, screen, clazz.simpleName)?.addToBackStack(clazz.simpleName)?.commit()
    }


    override fun getContext(): Context {
        return super.requireContext()
    }

    fun <V : View> findViewById(id: Int): V {
        return fullview?.findViewById(id) ?: throw RuntimeException("fullview is null")
    }

    fun notify(lambda: P.() -> Unit) {
        presenter?.apply(lambda) ?: throw RuntimeException("Presenter must not be null at this point")
    }

    open fun onBackPressed(): Boolean = false

}




