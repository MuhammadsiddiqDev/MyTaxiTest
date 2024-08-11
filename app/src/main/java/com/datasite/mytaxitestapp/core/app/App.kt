package com.datasite.mytaxitestapp.core.app

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
        companion object {
            var instance: Application? = null
                private set
        }

//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base?.let { LocaleManager.setLang(it) })
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        LocaleManager.setLang(this)
//    }
//}