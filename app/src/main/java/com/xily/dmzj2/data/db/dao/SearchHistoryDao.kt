package com.xily.dmzj2.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.xily.dmzj2.data.db.entity.SearchHistory

/**
 * Created by Xily on 2019/10/5.
 */
@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM SearchHistory ORDER BY id DESC")
    fun getSearchHistory(): LiveData<List<SearchHistory>>

    @Query("DELETE FROM SearchHistory")
    suspend fun clearSearchHistory()

    @Insert
    suspend fun addSearchHistory(searchHistory: SearchHistory): Long
}