package com.xily.dmzj2.data.remote.model

import java.io.Serializable


data class LoginBean(
    var result: Int, // 1
    var msg: String, // OK
    var data: Data
) {

    data class Data(
        var uid: String,
        var nickname: String,
        var dmzj_token: String,
        var photo: String,
        var bind_phone: String,
        var email: String,
        var passwd: String
    ) : Serializable
}