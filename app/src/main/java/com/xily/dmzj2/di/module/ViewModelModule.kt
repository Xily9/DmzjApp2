package com.xily.dmzj2.di.module

import com.xily.dmzj2.ui.history.HistoryViewModel
import com.xily.dmzj2.ui.info.InfoViewModel
import com.xily.dmzj2.ui.login.LoginViewModel
import com.xily.dmzj2.ui.main.HomeIndexViewModel
import com.xily.dmzj2.ui.main.HomeLatestViewModel
import com.xily.dmzj2.ui.main.MainViewModel
import com.xily.dmzj2.ui.rank.HomeRankViewModel
import com.xily.dmzj2.ui.read.ReadViewModel
import com.xily.dmzj2.ui.search.SearchViewModel
import com.xily.dmzj2.ui.subscribe.SubscribeViewModel
import com.xily.dmzj2.ui.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Xily on 2019/10/5.
 */
val viewModelModule = module {
    viewModel { (comicId: Int) ->
        InfoViewModel(comicId, get())
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
    viewModel {
        HomeRankViewModel(get())
    }
}