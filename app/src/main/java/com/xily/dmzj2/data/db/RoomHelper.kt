package com.xily.dmzj2.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import com.xily.dmzj2.data.db.dao.SearchHistoryDao
import com.xily.dmzj2.data.db.entity.SearchHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override suspend fun clearSearchHistory() = withContext(Dispatchers.IO) {
        searchHistoryDao.clearSearchHistory()
    }

    override suspend fun addSearchHistory(string: String) {
        withContext(Dispatchers.IO) {
            searchHistoryDao.addSearchHistory(SearchHistory(string))
        }
    }

}