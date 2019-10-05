package com.xily.dmzj2.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Xily on 2019/10/5.
 */
@Entity
data class SearchHistory(var word: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}