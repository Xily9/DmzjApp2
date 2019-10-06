package com.xily.dmzj2.ui.info

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager
import com.xily.dmzj2.data.remote.model.ReInfoBean
import com.xily.dmzj2.data.remote.model.SubscribeStatusBean

/**
 * Created by Xily on 2019/10/5.
 */
class InfoViewModel(private val dataManager: DataManager) : BaseViewModel() {
    suspend fun getComic(comicId: String) = dataManager.getComic(comicId)

    suspend fun getSubscribeStatus(comicId: String): SubscribeStatusBean {
        dataManager.userData?.let {
            return dataManager.getSubscribeStatus(it.uid, comicId)
        } ?: throw RuntimeException()
    }

    suspend fun getReadInfo(comicId: String): ReInfoBean {
        dataManager.userData?.let {
            return dataManager.getReadInfo(it.uid, comicId)
        } ?: throw RuntimeException()
    }
}