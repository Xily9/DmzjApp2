package com.xily.dmzj2.data.remote.model


data class CommentBean(
        var commentIds: List<String>,
        var comments: String,
        var total: Int // 5801
) {

        data class Comment(
                var id: String, // 5597809
                var obj_id: String, // 15982
                var content: String, // 萌系大冒险
                var sender_uid: String, // 101890584
                var is_goods: String, // 0
                var upload_images: String,
                var like_amount: String, // 0
                var create_time: String, // 1531524961
                var origin_comment_id: String, // 0
                var sex: String, // 2
                var nickname: String, // 利·姆露
                var avatar_url: String // https://avatar.dmzj.com/96/66/9666fc12287f8b251d862cfe9edfa6a7.png
        )
}