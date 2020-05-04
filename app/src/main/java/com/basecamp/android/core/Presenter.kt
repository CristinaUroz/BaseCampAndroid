package com.basecamp.android.core

import cc.popkorn.annotations.Exclude
import cc.popkorn.inject
import com.basecamp.android.domain.usecases.AnalyticsUseCase
import java.util.*
import kotlin.reflect.KClass

@Exclude
abstract class Presenter<V : BaseContract.View, R : BaseContract.Router> : BaseContract.Presenter {
    private val crashlyticsUseCase = inject<AnalyticsUseCase>()

    private var view: V? = null
    private var router: R? = null

    private val pendingViewTransactions = LinkedList<V.() -> Unit>()
    private val pendingRouterTransactions = LinkedList<R.() -> Unit>()
    private val pendingActionTransactions = LinkedList<ActionTmp<out Action>>()

    abstract fun getPageName(): String

    inner class ActionTmp<A : Action>(val clazz: KClass<A>, val lambda: A.() -> Unit)


    private fun onViewAttached() {}
    private fun onViewDetached() {}
    private fun onRouterAttached() {}
    private fun onRouterDetached() {}


    override fun destroy() {}


    //VISTA
    @Synchronized
    fun <V2 : BaseContract.View> attachView(view: V2) {
        if (this.view === view) return
        this.view = view as V
        processPendingViewTransactions()
        onViewAttached()
    }


    @Synchronized
    fun detachView() {
        this.view = null
        onViewDetached()
    }

    @Synchronized
    protected fun draw(lambda: V.() -> Unit) {
        view?.apply(lambda) ?: pendingViewTransactions.offer(lambda)
    }

    private fun processPendingViewTransactions() {
        view?.also {
            while (!pendingViewTransactions.isEmpty()) pendingViewTransactions.poll().invoke(it)
        }
    }


    //ROUTER
    @Synchronized
    fun <R2 : BaseContract.Router> attachRouter(router: R2) {
        if (this.router === router) return
        this.router = router as R
        crashlyticsUseCase.setScreen(getPageName())
        processPendingRouterTransactions()
        processPendingActionTransactions<Action>()
        onRouterAttached()
    }

    @Synchronized
    fun detachRouter() {
        router = null
        onRouterDetached()
    }

    @Synchronized
    protected fun navigate(lambda: R.() -> Unit) {
        router?.apply(lambda) ?: pendingRouterTransactions.offer(lambda)
    }

    private fun processPendingRouterTransactions() {
        router?.also {
            while (!pendingRouterTransactions.isEmpty()) pendingRouterTransactions.poll().invoke(it)
        }
    }


    //ACTIONS
    @Synchronized
    protected fun <A : Action> delegate(clazz: KClass<A>, lambda: A.() -> Unit) {
        router?.apply { getAction(clazz).also(lambda) } ?: pendingActionTransactions.offer(ActionTmp(clazz, lambda))
    }

    private fun <A : Action> processPendingActionTransactions() {
        router?.apply {
            while (!pendingActionTransactions.isEmpty()) {
                val poll = pendingActionTransactions.poll() as ActionTmp<A>
                getAction(poll.clazz).also(poll.lambda)
            }
        }
    }


}