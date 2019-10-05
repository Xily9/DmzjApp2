package com.xily.dmzj2.data.db

import androidx.lifecycle.LiveData

/**
 * Created by Xily on 2019/10/5.
 */
interface DbHelper {
    fun getSearchHistory(): LiveData<List<String>>
    fun clearSearchHistory()
    fun addSearchHistory(string: String)
}