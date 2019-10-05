package com.xily.dmzj2.data.pref

import com.xily.dmzj2.data.remote.model.LoginBean

/**
 * Created by Xily on 2019/10/5.
 */
class SharedPreferencesHelper : PrefHelper {
    override var checkUpdate: Boolean by PrefDelegate(false)
    override var UserData: LoginBean.Data? by PrefDelegate(null)
}