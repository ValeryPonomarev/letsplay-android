package com.easysales.letsplay.data.article

import com.easysales.letsplay.data.BaseRepository
import com.easysales.letsplay.data.SchedulerProvider
import com.easysales.letsplay.data.db.Database
import com.easysales.letsplay.data.http.HttpClient
import com.easysales.letsplay.data.storage.AssetsStorage
import com.easysales.letsplay.utils.numbers.randomBoolean
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    httpClient: HttpClient,
    schedulerProvider: SchedulerProvider,
    gson: Gson,
    private val database: Database,
    private val assetsStorage: AssetsStorage
) : BaseRepository(httpClient, schedulerProvider, gson), ArticleRepository {

    override fun getArticleById(id: String): Maybe<Article> = database.articleDao().getById(id).subscribeOn(schedulerProvider.io())

    override fun getArticleHtml(articleId: String): Maybe<String> {
        //TODO FAKE
        val html: String
        if (randomBoolean()) {
            html = assetsStorage.readTextFromFile("1.html")
        } else {
            html = assetsStorage.readTextFromFile("2.html")
        }

        return Maybe.just(html)
            .subscribeOn(schedulerProvider.io());
    }

    override fun observeArticle(id: String): Flowable<ArticleInfo> =
        database.articleDao().observe(id)
            .distinctUntilChanged()
            .subscribeOn(schedulerProvider.io())

    override fun observeAll(): Flowable<List<ArticleInfo>> {
        val fromCache = database.articleDao().observeAll().debounce(Database.CHANGE_DEBOUNCE, TimeUnit.MILLISECONDS, schedulerProvider.io())
        return Single.fromCallable { isCached() }
            .flatMapPublisher { isCached ->
                if(isCached) {
                    fromCache
                }
                else {
                    reload().andThen(fromCache)
                }
            }
            .subscribeOn(schedulerProvider.io())
    }

    override fun reload(): Completable {
        return getHttpClient().getArticles()
            .flatMapCompletable { data: List<ArticleResponse> ->

                val articles = ArrayList<Article>(data.size)
                val categories = HashMap<Long, Category>()
                val articleCategories = ArrayList<ArticleCategory>()

                data.forEach { articleResponse ->
                    val article = articleResponse.toDb()
                    articles.add(article)

                    articleResponse.categories.forEach { categoryResponse ->
                        val category = categories.getOrPut(
                            categoryResponse.apiId,
                            { categoryResponse.toDb() }
                        )

                        val articleCategory = ArticleCategory(article.id, category.id)
                        articleCategories.add(articleCategory)
                    }
                }

                return@flatMapCompletable Completable.fromCallable {
                    database.categoryDao().deleteAll()
                    database.categoryDao().insertAll(categories.values)
                    database.articleDao().deleteAll()
                    database.articleDao().insertAll(articles)
                    database.articleCategoryDao().deleteAll()
                    database.articleCategoryDao().insertAll(articleCategories)
                }
            }
            .subscribeOn(schedulerProvider.io())
    }

    override fun addToFavorite(article: com.easysales.letsplay.domain.article.Article): Completable {
        return Completable.fromCallable {
                val favorite = Favorite(Database.generateId(), article.apiId);
                database.favoriteDao().insert(favorite)
            }
            .subscribeOn(schedulerProvider.io())
    }

    override fun removeFromFavorite(article: com.easysales.letsplay.domain.article.Article): Completable {
        return database.favoriteDao().getByArticleApiId(article.apiId)
            .flatMapCompletable {
                Completable.fromCallable { database.favoriteDao().delete(it) }
            }
            .subscribeOn(schedulerProvider.io())
    }

    private fun isCached(): Boolean {
        return database.articleDao().haveItems()
    }
}
