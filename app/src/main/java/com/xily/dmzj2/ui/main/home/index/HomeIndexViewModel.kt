package com.xily.dmzj2.ui.main.home.index

import androidx.lifecycle.MutableLiveData
import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager
import com.xily.dmzj2.data.remote.model.RecommendBean
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

/**
 * Created by Xily on 2019/10/5.
 */
class HomeIndexViewModel(private val dataManager: DataManager) : BaseViewModel() {
    val recommend = MutableLiveData<List<RecommendBean>>()
    fun getRecommend() {
        launch {
            val asyncList = arrayListOf<Deferred<List<RecommendBean>>>()
            asyncList += async { dataManager.getRecommend() }
            asyncList += async {
                dataManager.userData?.let {
                    arrayListOf(dataManager.getMineRecommend("49", it.uid).data)
                } ?: arrayListOf()
            }
            asyncList += async {
                dataManager.userData?.let {
                    arrayListOf(dataManager.getMineRecommend("50", it.uid).data)
                } ?: arrayListOf()
            }
            val list = arrayListOf<RecommendBean>()
            asyncList.forEach {
                list += it.await()
            }
            recommend.value = list
        }
    }

}