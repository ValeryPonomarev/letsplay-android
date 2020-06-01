package com.easysales.letsplay.domain.article

import com.easysales.letsplay.R
import com.easysales.letsplay.data.article.ArticleRepository
import com.easysales.letsplay.data.exception.NotFoundException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

internal class ArticleServiceImpl(
    private val articleRepository: ArticleRepository
) : ArticleService {
    override fun reload(): Completable {
        return articleRepository.reload()
    }

    override fun observeArticles(): Flowable<List<Article>> {
        return articleRepository.observeAll()
            .map { articles ->  articles.map { it.fromDb() } }
    }

    override fun observeArticle(articleId: String): Flowable<Article> {
        return articleRepository.observeArticle(articleId)
            .map { it.fromDb() }
    }

    override fun getArticleHtml(articleId: String): Single<String> {
        return articleRepository.getArticleHtml(articleId)
            .switchIfEmpty(Single.error { NotFoundException(R.string.error_article_notFound) })
    }

    override fun addToFavorite(article: Article): Completable {
        return articleRepository.addToFavorite(article)
    }

    override fun removeFromFavorite(article: Article): Completable {
        return articleRepository.removeFromFavorite(article)
    }
}