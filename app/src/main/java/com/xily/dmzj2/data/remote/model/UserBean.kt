package com.xily.dmzj2.data.remote.model


data class UserBean(
        var nickname: String, // dmzj_106216420
        var description: String,
        var birthday: String, // 0000-00-00
        var sex: Int, // 1
        var cover: String, // http://images.dmzj.com/user/5b/4c/5b4c98d275d9c668bdea802ad6151b41.png
        var blood: Int, // 0
        var constellation: String,
        var bind_phone: Long, // 13295978869
        var email: String,
        var channel: String, // qq
        var is_verify: Int, // 0
        var is_modify_name: Int, // 1
        var data: List<Any>,
        var amount: Int, // 0
        var is_set_pwd: Int, // 1
        var bind: List<Bind>
) {

    data class Bind(
            var type: String, // qq
            var name: String // dmzj_106216420
    )
}