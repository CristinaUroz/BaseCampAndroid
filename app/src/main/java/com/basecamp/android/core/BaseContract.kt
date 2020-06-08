package com.basecamp.android.core

import android.os.Bundle
import cc.popkorn.annotations.Exclude
import kotlin.reflect.KClass

interface BaseContract {

    interface View

    @Exclude
    interface Presenter {
        fun init(bundle: Bundle)
        fun destroy()
    }

    interface Router {
        fun <A : Action> getAction(clazz: KClass<A>): A
    }
}