package com.xily.dmzj2.ui.main

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager

/**
 * Created by Xily on 2019/10/5.
 */
class MainViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getUserData() = dataManager.userData
}