package com.easysales.letsplay.domain.article

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ArticleService {
    fun reload(): Completable
    fun observeArticles(): Flowable<List<Article>>
    fun observeArticle(articleId: String): Flowable<Article>
    fun getArticleHtml(articleId: String): Single<String>
    fun addToFavorite(article: Article): Completable
    fun removeFromFavorite(article: Article): Completable
}