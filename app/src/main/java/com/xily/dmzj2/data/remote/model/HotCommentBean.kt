package com.xily.dmzj2.data.remote.model

data class HotCommentBean(
    var content: String, // 哇，我在脸上长过几次，每次发现就直接拔了
    var cover: String, // https://avatar.dmzj.com/4a/70/4a704287c1083ce459c8322a0d4f3bf1.png
    var create_time: Int, // 1570721344
    var hot_comment_amount: Int, // 0
    var id: Int, // 21163836
    var is_goods: Int, // 0
    var is_passed: Int, // 1
    var like_amount: Int, // 0
    var masterComment: List<MasterComment>,
    var masterCommentNum: Int, // 5
    var nickname: String,
    var obj_id: Int, // 9021
    var origin_comment_id: Int, // 0
    var reply_amount: Int, // 0
    var sender_uid: Int, // 104447238
    var sex: Int, // 2
    var to_comment_id: Int, // 21153436
    var to_uid: Int, // 101129066
    var top_status: Int, // 0
    var upload_images: String
) {
    data class MasterComment(
        var content: String, // 对对对，我也是这样的，就是突然间就出现了，之前完全没有发现过这东西。
        var cover: String, // https://avatar.dmzj.com/75/2c/752c4b3e6a4075652a217312f86fe02c.png
        var create_time: Int, // 1570700884
        var hot_comment_amount: Int, // 1
        var id: Int, // 21153436
        var is_goods: Int, // 0
        var is_passed: Int, // 1
        var like_amount: Int, // 0
        var nickname: String, // Decade.梁
        var obj_id: Int, // 9021
        var origin_comment_id: Int, // 0
        var reply_amount: Int, // 1
        var sender_uid: Int, // 101129066
        var sex: Int, // 1
        var to_comment_id: Int, // 21117740
        var to_uid: Int, // 104539543
        var top_status: Int, // 0
        var upload_images: String
    )
}