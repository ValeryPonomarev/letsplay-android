package com.easysales.letsplay.data.fake

import com.easysales.letsplay.data.article.ArticleResponse
import com.easysales.letsplay.data.article.CategoryResponse
import com.easysales.letsplay.data.http.HttpClient
import com.easysales.letsplay.utils.LogUtils
import com.easysales.letsplay.utils.numbers.randomInt
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FakeHttpClient: HttpClient {

    private val avatarUrl = "http://fandea.ru/upload/000/u1/013/f9c39cfc.jpg"
    private val fakeFactory = FakeFactory()

    override fun getArticles(): Single<List<ArticleResponse>> {
        LogUtils.d("FAKE", "getArticles")
        val response = ArrayList<ArticleResponse>()

        val categories = ArrayList<CategoryResponse>()
        for(id in 1..10) {
            categories.add(CategoryResponse(id.toLong(), "category $id"))
        }

        for(id in 1..10) {

            val countCategories = randomInt(2, 10)
            val articleCategories = ArrayList<CategoryResponse>(countCategories)
            for (categoryIndex in 0..countCategories) {
                articleCategories.add(fakeFactory.getCategoryResponse(categoryIndex))
            }

            val articleResponse = fakeFactory.getArticleResponse(id, articleCategories)

//            val articleResponse = ArticleResponse(
//                id.toLong(),
//                Date().time,
//                Date().time,
//                "Прятки для самых маленьких $id",
//                "Помимо игр с предметами, можно поиграть с ребенком в простые игры, например \"КУ-КУ\". Такая незамысловатая игра сформирует у ребенка понимание, что даже если вы ушли (спрятались за ладошками), вы обязательно к нему вернетесь и будете всегда рядом. А когда малыш начнет понимать, что вы спрятались, ему будет интересно вас «отыскать». $id",
//                avatarUrl,
//                articleCategories,
//                id
//            )

            response.add(articleResponse)
        }

        return fakeRequest.andThen(Single.just(response as List<ArticleResponse>))
    }

    private val fakeRequest : Completable = Completable.defer {
        Observable
            .timer(1L, TimeUnit.SECONDS, Schedulers.io()).map { t -> t.toString() }
            .ignoreElements()
    }

}