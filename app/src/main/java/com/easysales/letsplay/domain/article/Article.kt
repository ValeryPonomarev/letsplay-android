package com.easysales.letsplay.domain.article

import java.util.*
import kotlin.collections.ArrayList

class Article {
    val id: String
    val apiId: Long
    val dateChanged: Date
    val dateCreated: Date
    val title: String
    val description: String
    val imageUrl: String?
    val categories: Collection<Category>
    val likes: Int
    val isFavorite: Boolean

    public constructor(
        id: String,
        apiId: Long,
        title: String,
        description: String,
        imageUrl: String? = null,
        categories: Collection<Category> = ArrayList(),
        dateChanged: Date = Date(),
        dateCreated: Date = Date(),
        likes: Int,
        isFavorite: Boolean
    ) {
        this.id = id
        this.apiId = apiId
        this.title = title
        this.description = description
        this.imageUrl = imageUrl
        this.categories = categories
        this.dateChanged = dateChanged
        this.dateCreated = dateCreated
        this.likes = likes
        this.isFavorite = isFavorite
    }
}

fun com.easysales.letsplay.data.article.ArticleInfo.fromDb() : Article = Article(
    this.article.id,
    this.article.apiId,
    this.article.title,
    this.article.description,
    this.article.previewUrl,
    this.categories.map { it.fromDb() },
    this.article.dateChanged,
    this.article.dateCreated,
    this.article.likes,
    this.favorite != null
)
