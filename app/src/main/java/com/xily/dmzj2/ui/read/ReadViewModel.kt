package com.xily.dmzj2.ui.read

import com.xily.dmzj2.base.BaseViewModel
import com.xily.dmzj2.data.DataManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Xily on 2019/10/5.
 */
class ReadViewModel(private val dataManager: DataManager) : BaseViewModel() {
    fun getComic(comicId: String) = getData {
        dataManager.getComic(comicId)
    }

    fun getChapter(comicId: String, chapterId: String) = getData {
        dataManager.getChapter(comicId, chapterId)
    }

    fun recordRead(comicId: String, chapterId: String, page: Int, time: String) {
        dataManager.userData?.let {
            val jsonArray = JSONArray()
            val jsonObject = JSONObject()
            jsonObject.put(comicId, chapterId)
            jsonObject.put("comicId", comicId)
            jsonObject.put("chapterId", chapterId)
            jsonObject.put("page", page)
            jsonObject.put("time", time)
            jsonArray.put(jsonObject)
            val queryMap = mutableMapOf<String, String>()
            queryMap["st"] = "comic"
            queryMap["uid"] = it.uid
            queryMap["callback"] = "record_jsonpCallback"
            queryMap["json"] = jsonArray.toString()
            queryMap["type"] = "3"
            GlobalScope.launch {
                dataManager.recordRead(queryMap)
            }
        }
    }
}