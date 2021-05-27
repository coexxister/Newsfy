package com.example.newsfy_rework.data

import java.io.Serializable

class News: Serializable {
    var title: String? = null
    var author: String? = null
    var description: String? = null
    var urlToImage: String? = null
    var date: String? = null

    constructor() {}
    constructor(
        title: String?,
        authorAndDate: String?,
        description: String?,
        urlToImage: String?,
        date: String? = null
    ) {
        this.title = title
        this.author = authorAndDate
        this.description = description
        this.urlToImage = urlToImage
        this.date = date
    }
}