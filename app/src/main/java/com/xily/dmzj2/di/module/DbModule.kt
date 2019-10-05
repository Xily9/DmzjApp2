package com.xily.dmzj2.di.module

import androidx.room.Room
import com.xily.dmzj2.data.db.DbHelper
import com.xily.dmzj2.data.db.RoomDatabase
import com.xily.dmzj2.data.db.RoomHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by Xily on 2019/10/5.
 */
val dbModule = module {
    factory {
        Room.databaseBuilder(
            androidApplication(),
            RoomDatabase::class.java, "dmzj2"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        get<RoomDatabase>().searchHistoryDao()
    }

    factory<DbHelper> {
        RoomHelper(get())
    }
}