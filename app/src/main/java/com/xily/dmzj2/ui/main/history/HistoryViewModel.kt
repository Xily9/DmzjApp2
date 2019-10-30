package com.xily.dmzj2.ui.main.history

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager

/**
 * Created by Xily on 2019/10/5.
 */
class HistoryViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getHistory() = getResultData {
        val userData = dataManager.userData
        if (userData == null) {
            Result.failure(Exception("请先登录!"))
        } else {
            Result.success(dataManager.getHistory(userData.uid))
        }
    }
}