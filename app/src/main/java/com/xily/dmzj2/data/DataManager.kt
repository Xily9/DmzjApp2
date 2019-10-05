package com.xily.dmzj2.data

import com.xily.dmzj2.data.db.DbHelper
import com.xily.dmzj2.data.pref.PrefHelper
import com.xily.dmzj2.data.remote.HttpHelper

/**
 * Created by Xily on 2019/10/5.
 */
class DataManager(httpHelper: HttpHelper, dbHelper: DbHelper, prefHelper: PrefHelper) :
    HttpHelper by httpHelper, DbHelper by dbHelper, PrefHelper by prefHelper