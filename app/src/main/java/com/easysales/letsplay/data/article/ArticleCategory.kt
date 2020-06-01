package com.easysales.letsplay.data.article

import androidx.room.*
import com.easysales.letsplay.data.db.BaseDao

@Entity(
    tableName = "article_category",
    primaryKeys = ["articleId", "categoryId"]
)
data class ArticleCategory(
    @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo(name = "categoryId") val categoryId: String
)

@Dao
interface ArticleCategoryDao: BaseDao<ArticleCategory> {
    @Query("delete from article_category")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: Collection<ArticleCategory>)
}