package com.xily.dmzj2.ui.main.home.rank

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager

/**
 * Created by Xily on 2019/10/7.
 */
class HomeRankViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getRank(filter: String, day: String, type: String, page: String) = getData {
        dataManager.getRank(filter, day, type, page)
    }

    fun getRankFilter() = getData {
        dataManager.getRankFilter()
    }
}