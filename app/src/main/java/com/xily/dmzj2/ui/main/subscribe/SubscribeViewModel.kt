package com.xily.dmzj2.ui.main.subscribe

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager

/**
 * Created by Xily on 2019/10/5.
 */
class SubscribeViewModel(private var dataManager: DataManager) : BaseViewModel() {
    fun getSubscribe() = getResultData {
        val userData = dataManager.userData
        if (userData == null) {
            Result.failure(Exception("请先登录!"))
        } else {
            Result.success(dataManager.getSubscribe(userData.uid, userData.dmzj_token, "0"))
        }
    }
}