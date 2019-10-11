package com.xily.dmzj2.ui.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager
import com.xily.dmzj2.data.remote.model.ComicBean
import com.xily.dmzj2.data.remote.model.ReInfoBean
import com.xily.dmzj2.data.remote.model.SubscribeStatusBean
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

/**
 * Created by Xily on 2019/10/5.
 */
class InfoViewModel(val comicId: Int, val dataManager: DataManager) : BaseViewModel() {
    val comicBean = MutableLiveData<ComicBean>()
    val subscribeStatusBean = MutableLiveData<SubscribeStatusBean>()
    val reInfoBean = MutableLiveData<ReInfoBean>()
    val chaptersBean = map(comicBean) {
        it.chapters
    }

    fun getComic() {
        launch {
            val asyncList = arrayListOf<Deferred<*>>()
            asyncList += async {
                comicBean.value = try {
                    dataManager.getComic(comicId.toString())
                } catch (e: Exception) {
                    null
                }
            }
            dataManager.userData?.let {
                asyncList += async {
                    subscribeStatusBean.value = try {
                        dataManager.getSubscribeStatus(it.uid, comicId.toString())
                    } catch (e: Exception) {
                        null
                    }
                }
                asyncList += async {
                    reInfoBean.value = try {
                        dataManager.getReadInfo(it.uid, comicId.toString())
                    } catch (e: Exception) {
                        null
                    }
                }
            } ?: let {
                subscribeStatusBean.value = null
                reInfoBean.value = null
            }
            asyncList.forEach {
                it.await()
            }
        }
    }

    suspend fun getComment(page: String, limit: String) =
        dataManager.getComments(comicId.toString(), page, limit)

    suspend fun getTopComment() = dataManager.getTopComment(comicId.toString())

    suspend fun getHotComment(page: String) = dataManager.getHotComments(comicId.toString(), page)
}