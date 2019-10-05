package com.xily.dmzj2.data.remote.model


data class ClassifyFilterBean(
        var title: String, // 地域
        var items: List<Item>
) {

    data class Item(
            var tag_id: Int, // 8435
            var tag_name: String // 其他
    )
}