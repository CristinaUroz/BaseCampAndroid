package com.basecamp.android.data.datasources.preferences.engine

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

internal abstract class Preferences(ctx : Context, file : String) {

    private val gson = GsonBuilder().run {
        enableComplexMapKeySerialization()
        excludeFieldsWithModifiers(Modifier.STATIC)
        create()
    }

    private val preferences = ctx.getSharedPreferences(file, Context.MODE_PRIVATE)


    protected fun <T:Any> putList(key: String, value: List<T>) = preferences.edit().putString(key, value.toJSon()).apply()

    protected inline fun <reified T:Any> getList(key: String, def:List<T> = arrayListOf()) = preferences.getString(key, null)?.jsonToList(T::class) ?: def


    protected fun <E:Enum<E>> putEnum(key: String, value: E) = preferences.edit().putInt(key, value.ordinal).apply()

    protected inline fun <reified E:Enum<E>> getEnum(key: String, def:E) = preferences.takeIf { it.contains(key) }?.getInt(key, 0)?.let { enumValues<E>()[it] } ?: def


    protected fun putString(key: String, value: String) = preferences.edit().putString(key, value).apply()

    protected fun getString(key: String) = preferences.getString(key, null)

    protected fun getString(key: String, def:String) = preferences.getString(key, def) ?: def


    protected fun putBoolean(key: String, value: Boolean) = preferences.edit().putBoolean(key, value).apply()

    protected fun getBoolean(key: String, def:Boolean=true) = preferences.getBoolean(key, def)


    protected fun putFloat(key: String, value: Float) = preferences.edit().putFloat(key, value).apply()

    protected fun getFloat(key: String, def:Float=0f) = preferences.getFloat(key, def)


    fun clear() = preferences.edit().clear().apply()

    fun Any.toJSon(): String = gson.toJson(this)

    fun <T:Any> String.jsonToList(kclass1: KClass<T>): List<T> =
        TypeToken.getParameterized(List::class.java, kclass1.java)
            .let { gson.fromJson(this,it.type) ?: ArrayList() }

}