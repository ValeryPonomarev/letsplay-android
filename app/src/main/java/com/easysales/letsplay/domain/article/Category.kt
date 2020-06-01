package com.easysales.letsplay.domain.article

class Category {
    val id: String
    val apiId: Long
    val name: String

    public constructor(id: String, apiId: Long, name: String) {
        this.id = id
        this.apiId = apiId
        this.name = name
    }
}


fun com.easysales.letsplay.data.article.Category.fromDb(): Category {
    return Category(this.id, this.apiId, this.title)
}