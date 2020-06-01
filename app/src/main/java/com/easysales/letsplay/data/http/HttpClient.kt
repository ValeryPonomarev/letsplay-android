package com.easysales.letsplay.data.http

import com.easysales.letsplay.data.article.ArticleResponse
import io.reactivex.Single
import retrofit2.http.GET

interface HttpClient {
    @GET("api/v1/articles")
    fun getArticles(): Single<List<ArticleResponse>>
}