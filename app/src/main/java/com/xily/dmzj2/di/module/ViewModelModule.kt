package com.xily.dmzj2.di.module

import com.xily.dmzj2.ui.info.InfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Xily on 2019/10/5.
 */
val viewModelModule= module {
    viewModel { 
        InfoViewModel(get())
    }
}