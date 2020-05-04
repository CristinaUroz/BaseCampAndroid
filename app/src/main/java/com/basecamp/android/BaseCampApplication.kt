package com.basecamp.android

import android.app.Application
import android.content.Context
import cc.popkorn.getPopKornController

class BaseCampApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        getPopKornController().addInjectable(this, Context::class)
    }


}