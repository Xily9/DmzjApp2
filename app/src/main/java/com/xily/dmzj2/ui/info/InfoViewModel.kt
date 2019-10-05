package com.xily.dmzj2.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager
import com.xily.dmzj2.data.remote.model.ComicBean
import kotlinx.coroutines.withContext

/**
 * Created by Xily on 2019/10/5.
 */
class InfoViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getComic(comicId: String): MutableLiveData<ComicBean?> {
        val comicBean: MutableLiveData<ComicBean?> = MutableLiveData()
        launch(tryBlock = {
            comicBean.value = dataManager.getComic(comicId)
        }, catchBlock = {
            it.printStackTrace()
            comicBean.value = null
        })
        return comicBean
    }
}