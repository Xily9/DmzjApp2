package com.xily.dmzj2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xily.dmzj2.data.db.dao.SearchHistoryDao
import com.xily.dmzj2.data.db.entity.SearchHistory

/**
 * Created by Xily on 2019/10/5.
 */
@Database(entities = [SearchHistory::class], version = 2)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}