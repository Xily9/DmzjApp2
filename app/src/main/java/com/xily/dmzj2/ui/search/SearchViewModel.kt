package com.xily.dmzj2.ui.search

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager
import kotlinx.coroutines.Dispatchers

/**
 * Created by Xily on 2019/10/5.
 */
class SearchViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getSearchHistory() = dataManager.getSearchHistory()
    fun clearSearchHistory() {
        launch(Dispatchers.IO) {
            dataManager.clearSearchHistory()
        }
    }

    fun addSearchHistory(string: String) {
        launch(Dispatchers.IO) {
            dataManager.addSearchHistory(string)
        }
    }

    fun getSearchHot() = getData {
        dataManager.getSearchHot()
    }

    fun search(string: String, page: String) = getData {
        dataManager.search(string, page)
    }
}