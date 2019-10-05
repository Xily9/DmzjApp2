package com.xily.dmzj2.ui.info

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager

/**
 * Created by Xily on 2019/10/5.
 */
class InfoViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getComic(comicId: String) = getData {
        dataManager.getComic(comicId)
    }
}