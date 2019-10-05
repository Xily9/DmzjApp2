package com.xily.dmzj2.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import com.xily.dmzj2.data.db.dao.SearchHistoryDao
import com.xily.dmzj2.data.db.entity.SearchHistory

/**
 * Created by Xily on 2019/10/5.
 */
class RoomHelper(private val searchHistoryDao: SearchHistoryDao) : DbHelper {
    override fun getSearchHistory(): LiveData<List<String>> {
        return map(searchHistoryDao.getSearchHistory()) {
            it.map {
                it.word
            }
        }
    }

    override fun clearSearchHistory() {
        searchHistoryDao.clearSearchHistory()
    }

    override fun addSearchHistory(string: String) {
        searchHistoryDao.addSearchHistory(SearchHistory(string))
    }

}