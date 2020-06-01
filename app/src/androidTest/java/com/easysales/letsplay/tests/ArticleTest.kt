package com.easysales.letsplay.tests

import com.easysales.letsplay.BaseTest
import com.easysales.letsplay.data.http.HttpClient
import com.easysales.letsplay.domain.article.ArticleService
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

class ArticleTest: BaseTest() {

    @Inject lateinit var httpClient: HttpClient
    @Inject lateinit var articleService: ArticleService

    override fun setup() {
        super.setup()
        componentManager.appComponent?.inject(this)
    }

    @Test
    fun loadArticlesTest() {
        val categoryResponse1 = fakeFactory.getCategoryResponse(1)
        val categoryResponse2 = fakeFactory.getCategoryResponse(2)

        val articleResponse1 = fakeFactory.getArticleResponse(1, ArrayList());
        val articleResponse2 = fakeFactory.getArticleResponse(2, arrayListOf(categoryResponse1, categoryResponse2));

        Mockito.`when`(httpClient.getArticles()).thenReturn(Single.just(arrayListOf(articleResponse1, articleResponse2)))

        articleService.reload()
            .test()
            .assertComplete()

        articleService.observeArticles()
            .test()
            .assertValue { models ->
                models.sortedBy { it.apiId }
                assertEquals(2, models.size)

                val article1 = models[0]
                assertEquals(1, article1.apiId)
                assertEquals("title 1", article1.title)
                assertEquals("description 1", article1.description)
                assertEquals("previewUrl_1", article1.imageUrl)
                assertEquals(1, article1.likes)
                assertEquals(0, article1.categories.size)

                val article2 = models[1]
                assertEquals(2, article2.apiId)
                assertEquals("title 2", article2.title)
                assertEquals("description 2", article2.description)
                assertEquals("previewUrl_2", article2.imageUrl)
                assertEquals(2, article2.likes)
                assertEquals(2, article2.categories.size)

                val categories = article2.categories.sortedBy { it.apiId }
                assertEquals(1, categories[0].apiId)
                assertEquals(2, categories[1].apiId)

                return@assertValue true
            }

        Mockito.verify(httpClient).getArticles()
    }

    @Test
    fun addToFavoriteTest() {
        val category1 = fakeRepository.createCategory(1)
        val category2 = fakeRepository.createCategory(2)
        val article1 = fakeRepository.createArticle(1, false, category1)
        val article2 = fakeRepository.createArticle(1, false, category2)

        articleService.addToFavorite(article1)

    }
}
