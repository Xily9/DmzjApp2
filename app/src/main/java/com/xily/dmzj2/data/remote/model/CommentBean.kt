package com.xily.dmzj2.data.remote.model

data class CommentBean(
    var commentIds: List<String>,
    var comments: Map<String, Comment>,
    var total: Int // 5796
) {
    data class Comment(
        var avatar_url: String, // https://avatar.dmzj.com/63/ef/63effcc7cb94b7bd2f71a91e1874b2ce.png
        var content: String, // 感觉漫画更好看
        var create_time: String, // 1570643903
        var id: String, // 21133562
        var is_goods: String, // 0
        var like_amount: String, // 0
        var nickname: String, // 散華禮彌86816
        var obj_id: String, // 38334
        var origin_comment_id: String, // 0
        var sender_uid: String, // 105347895
        var sex: String, // 1
        var upload_images: String
    )
}