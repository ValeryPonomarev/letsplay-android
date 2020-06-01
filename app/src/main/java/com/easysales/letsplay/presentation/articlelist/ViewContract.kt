package com.easysales.letsplay.presentation.articlelist

import com.easysales.letsplay.domain.article.Article
import com.easysales.letsplay.presentation.core.ClickEvent
import com.easysales.letsplay.presentation.core.ViewState

data class ArticleItemDto(
    val id: String,
    val title: String,
    val description: String,
    val likes: String,
    val categories: List<String>
)

data class ViewDto(
    val items: List<ArticleItemDto>
) : ViewState

data class ArticleClickEvent(
    val articleId: String
) : ClickEvent

fun Article.toViewDto(): ArticleItemDto {
    return ArticleItemDto(this.id, this.title, this.description, this.likes.toString(), this.categories.map { it.name })
}
