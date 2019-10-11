package com.xily.dmzj2.ui.login

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager

/**
 * Created by Xily on 2019/10/5.
 */
class LoginViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun login(nickname: String, passwd: String) = getResultData {
        val result = dataManager.login(nickname, passwd)
        if (result.result == 1) {
            dataManager.userData = result.data
            Result.success(result.data)
        } else {
            Result.failure(Exception(result.msg))
        }
    }
}