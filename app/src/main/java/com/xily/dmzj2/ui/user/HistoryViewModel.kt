package com.xily.dmzj2.ui.user

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager

/**
 * Created by Xily on 2019/10/5.
 */
class HistoryViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getHistory() = getData {
        dataManager.userData?.let {
            dataManager.getHistory(it.uid)
        }
    }
}