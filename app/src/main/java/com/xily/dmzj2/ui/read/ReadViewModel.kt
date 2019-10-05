package com.xily.dmzj2.ui.read

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager

/**
 * Created by Xily on 2019/10/5.
 */
class ReadViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getChapter(comicId: String, chapterId: String) = getData {
        dataManager.getChapter(comicId, chapterId)
    }
}