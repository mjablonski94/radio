package com.tunein.components.koin

import android.app.Application
import com.tunein.model.service.koin.apiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object KoinStarter {
    @JvmStatic
    fun start(applicationContext: Application) {
        startKoin {
            androidContext(applicationContext)
            modules(apiModule, appModule)
        }
    }
}