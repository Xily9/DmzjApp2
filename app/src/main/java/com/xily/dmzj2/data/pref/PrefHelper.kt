package com.xily.dmzj2.data.pref

import com.xily.dmzj2.data.remote.model.LoginBean

/**
 * Created by Xily on 2019/10/5.
 */
interface PrefHelper {
    var checkUpdate: Boolean
    var UserData: LoginBean.Data?
}