package com.xily.dmzj2.data.remote.model

import com.google.gson.annotations.SerializedName


data class RecommendBean(
        var category_id: Int, // 56
        var title: String, // 最新上架
        var sort: Int, // 11
        var data: List<Data>
) {

    data class Data(
            @SerializedName(value = "obj_id", alternate = ["id"])
            var id: Int, // 45395
            var title: String, // 妖怪少女
            var type: Int,
            var url: String,
            var authors: String, // suい
            var status: String, // 已完结
            var cover: String // https://images.dmzj.com/webpic/8/yaoguai20180815.jpg
    )
}