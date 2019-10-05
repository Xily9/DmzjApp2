package com.xily.dmzj2.di.module

import com.xily.dmzj2.data.DataManager
import org.koin.dsl.module

/**
 * Created by Xily on 2019/10/5.
 */
val dataModule = module {
    single {
        DataManager(get(), get(), get())
    }
} + httpModule + dbModule + prefModule