package com.basecamp.android.data.repositories.datasources

interface SettingsPreferences {

    fun setEmail(email: String)
    fun getEmail() : String?

    fun setCanEnableDarkMode(canEnableDarkMode: Boolean)
    fun getCanEnableDarkMode() : Boolean

    fun setDarkMode(darkMode: Boolean)
    fun getDarkMode() : Boolean

    fun clear()

}