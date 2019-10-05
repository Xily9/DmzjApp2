package com.xily.dmzj2.data.remote.model


data class MineRecommendBean(
        var code: Int, // 0
        var msg: String,
        var data: Data
) {

    data class Data(
            var category_id: Int, // 49
            var title: String, // 我的订阅
            var sort: Int, // 2
            var data: List<Data1>
    ) {

        data class Data1(
                var uid: Int, // 106216420
                var id: Int, // 39695
                var title: String, // 五等分的花嫁
                var authors: String, // 春场ねぎ
                var cover: String, // https://images.dmzj.com/webpic/5/wudengfendehuajia0702fj.jpg
                var status: String // 连载中
        )
    }
}