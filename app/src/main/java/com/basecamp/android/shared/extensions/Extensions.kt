package com.basecamp.android.shared.extensions

import kotlin.reflect.KClass

fun <T : Any> KClass<T>.newInstance(): T {
    return this.constructors.first().call()
}

fun <T : Any> KClass<T>.isAssignableFrom(obj: Any): Boolean {
    return this.java.isAssignableFrom(obj.javaClass)
}

fun <T : Any> KClass<T>.isAssignableFrom(clazz: KClass<*>): Boolean {
    return this.java.isAssignableFrom(clazz.java)
}

fun <T : Any> KClass<T>.isInterface(): Boolean {
    return this.java.isInterface
}

fun <A : Annotation> KClass<*>.getAnnotation(annotation: KClass<A>): A {
    return this.annotations.find { it.annotationClass.equals(annotation) }!! as A
}

