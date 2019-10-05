package com.xily.dmzj2.data.remote.model


data class ComicBean(
        var id: Int, // 7598
        var islong: Int, // 2
        var direction: Int, // 1
        var title: String, // 黑社会的超能力女儿
        var is_dmzj: Int, // 0
        var cover: String, // http://images.dmzj.com/webpic/11/160131heishehuifml.jpg
        var description: String, // 黑社会干部与从天而降有超能力的女儿时而三次时而五次的幸福生活
        var last_updatetime: Int, // 1528269681
        var copyright: Int, // 0
        var first_letter: String, // h
        var hot_num: Int, // 63806118
        var hit_num: Int, // 710774896
        var uid: String?, // null
        var types: List<Type>,
        var status: List<Statu>,
        var authors: List<Author>,
        var subscribe_num: Int, // 296703
        var chapters: List<Chapter>,
        var comment: Comment
) {

    data class Chapter(
            var title: String, // 单行本
            var data: List<Data>
    ) {

        data class Data(
                var chapter_id: Int, // 24747
                var chapter_title: String, // 1卷
                var updatetime: Int, // 1382808052
                var filesize: Int, // 35599904
                var chapter_order: Int // 1
        )
    }


    data class Comment(
            var comment_count: Int, // 11510
            var latest_comment: List<LatestComment>
    ) {

        data class LatestComment(
                var comment_id: Int, // 5600793
                var uid: Int, // 104785800
                var content: String, // 找到了，接41话
                var createtime: Int, // 1531539255
                var nickname: String, // 大连哪有阿瓦隆
                var avatar: String // http://images.dmzj.com/user/4b/94/4b94d7299fc22281cad96c4532bf8694.png
        )
    }


    data class Type(
            var tag_id: Int, // 7568
            var tag_name: String // 搞笑
    )


    data class Statu(
            var tag_id: Int, // 2309
            var tag_name: String // 连载中
    )


    data class Author(
            var tag_id: Int, // 4077
            var tag_name: String // 大武政夫
    )
}