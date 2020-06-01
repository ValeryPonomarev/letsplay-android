package com.easysales.letsplay.data.fake

import com.easysales.letsplay.data.article.Article
import com.easysales.letsplay.data.article.ArticleCategory
import com.easysales.letsplay.data.article.Category
import com.easysales.letsplay.data.article.Favorite
import com.easysales.letsplay.data.db.Database
import com.easysales.letsplay.domain.article.fromDb
import java.util.*

class FakeRepository(
    private val database: Database
) {
    fun createArticle(index: Int, isFavorite: Boolean = false, vararg categories: Category): com.easysales.letsplay.domain.article.Article {
        val article = Article(
            Database.generateId(),
            index.toLong(),
            Date(),
            Date(),
            "title $index",
            "description $index",
            "previewUrl_$index",
            index
        )
        database.articleDao().insert(article)

        if(isFavorite) {
            createFavoriteArticle(article.apiId)
        }

        categories.forEach { category ->
            createArticleCategory(article.id, category.id)
        }

        return database.articleDao().observe(article.id).blockingFirst().fromDb();
    }

    fun createFavoriteArticle(articleApiId: Long): Favorite {
        val favorite = Favorite(
            Database.generateId(),
            articleApiId
        )
        database.favoriteDao().insert(favorite)
        return favorite
    }

    fun createCategory(index: Int): Category {
        val category = Category(
            Database.generateId(),
            index.toLong(),
            "title $index"
        )
        database.categoryDao().insert(category)
        return category
    }

    fun createArticleCategory(articleId: String, categoryId: String): ArticleCategory {
        val category = ArticleCategory(
            articleId,
            categoryId
        )
        database.articleCategoryDao().insert(category)
        return category
    }
}