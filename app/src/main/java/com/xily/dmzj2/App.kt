package com.xily.dmzj2

import android.app.Application
import com.xily.dmzj2.di.module.dataModule
import com.xily.dmzj2.di.module.appModule
import com.xily.dmzj2.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Xily on 2019/10/5.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dataModule + appModule + viewModelModule)
        }
    }

    companion object {
        lateinit var instance: App
    }
}