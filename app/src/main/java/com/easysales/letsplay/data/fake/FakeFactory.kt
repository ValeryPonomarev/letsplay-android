package com.easysales.letsplay.data.fake

import com.easysales.letsplay.data.article.ArticleResponse
import com.easysales.letsplay.data.article.CategoryResponse
import java.util.*


class FakeFactory {
    fun getArticleResponse(index: Int, categories: List<CategoryResponse>): ArticleResponse {
        return ArticleResponse(
            index.toLong(),
            Date().time,
            Date().time,
            "title $index",
            "description $index",
            "previewUrl_$index",
            categories,
            index
        );
    }

    fun getCategoryResponse(index: Int): CategoryResponse {
        return CategoryResponse(
            index.toLong(),
            "title $index"
        )
    }
}