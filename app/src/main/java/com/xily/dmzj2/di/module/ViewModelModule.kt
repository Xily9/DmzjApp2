package com.xily.dmzj2.di.module

import com.xily.dmzj2.ui.info.InfoViewModel
import com.xily.dmzj2.ui.main.HomeIndexViewModel
import com.xily.dmzj2.ui.main.HomeLatestViewModel
import com.xily.dmzj2.ui.main.MainViewModel
import com.xily.dmzj2.ui.read.ReadViewModel
import com.xily.dmzj2.ui.search.SearchViewModel
import com.xily.dmzj2.ui.user.HistoryViewModel
import com.xily.dmzj2.ui.user.LoginViewModel
import com.xily.dmzj2.ui.user.SubscribeViewModel
import com.xily.dmzj2.ui.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Xily on 2019/10/5.
 */
val viewModelModule = module {
    viewModel {
        InfoViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        HomeIndexViewModel(get())
    }
    viewModel {
        HomeLatestViewModel(get())
    }
    viewModel {
        ReadViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        HistoryViewModel(get())
    }
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        SubscribeViewModel(get())
    }
    viewModel {
        UserViewModel(get())
    }
}