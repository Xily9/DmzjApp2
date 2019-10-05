package com.xily.dmzj2.di.module

import com.xily.dmzj2.data.pref.PrefHelper
import com.xily.dmzj2.data.pref.SharedPreferencesHelper
import org.koin.dsl.module

/**
 * Created by Xily on 2019/10/5.
 */
val prefModule= module {
    factory<PrefHelper> {
        SharedPreferencesHelper()
    }
}
