package com.xily.dmzj2.data.remote.model


data class SearchBean(
        var id: Int,
        var status: String,
        var title: String,
        var last_name: String,
        var cover: String,
        var authors: String,
        var types: String,
        var hot_hits: Int
)