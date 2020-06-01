package com.easysales.letsplay.data.article

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("id") val apiId: Long,
    @SerializedName("date_changed") val dateChanged: Long,
    @SerializedName("date_created") val dateCreated: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("categories") var categories: List<CategoryResponse>,
    @SerializedName("likes") val likes: Int
)

data class CategoryResponse(
    @SerializedName("id") val apiId: Long,
    @SerializedName("name") var title: String
)