package com.tunein

import android.app.Application
import com.tunein.components.koin.KoinStarter

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        KoinStarter.start(this)
    }
}