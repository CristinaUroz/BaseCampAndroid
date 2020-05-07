package com.basecamp.android

import android.app.Application
import android.content.Context
import cc.popkorn.getPopKornController
import com.google.firebase.auth.FirebaseAuth

class BaseCampApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        getPopKornController().addInjectable(this, Context::class)
        getPopKornController().addInjectable(FirebaseAuth.getInstance(), FirebaseAuth::class)
    }


}