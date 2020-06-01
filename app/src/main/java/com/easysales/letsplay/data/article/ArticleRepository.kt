package com.easysales.letsplay.data.article

import com.easysales.letsplay.data.BaseRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable

interface ArticleRepository {
    fun getArticleById(id: String): Maybe<Article>
    fun getArticleHtml(articleId: String): Maybe<String>
    fun observeArticle(id: String): Flowable<ArticleInfo>
    fun observeAll() : Flowable<List<ArticleInfo>>
    val warningsObservable: Observable<BaseRepository.Warning>
    fun reload(): Completable
    fun addToFavorite(article: com.easysales.letsplay.domain.article.Article): Completable
    fun removeFromFavorite(article: com.easysales.letsplay.domain.article.Article): Completable
}