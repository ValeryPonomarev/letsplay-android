package com.easysales.letsplay.data.article

import androidx.room.*
import com.easysales.letsplay.data.db.BaseDao
import com.easysales.letsplay.data.db.Database
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.util.*

@Entity(
    tableName = "article",
    indices = [Index(value = ["apiId"], unique = true)]
)
data class Article(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "apiId") val apiId: Long,
    @ColumnInfo(name = "dateChanged") val dateChanged: Date,
    @ColumnInfo(name = "dateCreated") val dateCreated: Date,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "previewUrl") val previewUrl: String?,
    @ColumnInfo(name = "likes") val likes: Int
)

fun ArticleResponse.toDb() : Article {
    val id = Database.generateId()
    return Article(
        id,
        this.apiId,
        Date(this.dateChanged),
        Date(this.dateCreated),
        this.title,
        this.description,
        this.previewUrl,
        this.likes
    )
}

data class ArticleInfo(
    @Embedded
    val article: Article,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ArticleCategory::class,
            entityColumn = "categoryId",
            parentColumn = "articleId"
        )
    )
    val categories: List<Category>,

    @Relation(
        parentColumn = "apiId",
        entityColumn = "articleApiId"
    )
    val favorite: Favorite?
)

@Dao
interface ArticleDao: BaseDao<Article> {
    @Transaction
    @Query("select * from article where id = :articleId")
    fun observe(articleId: String): Flowable<ArticleInfo>

    @Transaction
    @Query("select * from article")
    fun observeAll(): Flowable<List<ArticleInfo>>

    @Query("select * from article where id = :articleId")
    fun getById(articleId: String): Maybe<Article>
    
    @Query("delete from article")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: Collection<Article>)

    @Transaction
    fun updateAll(items: Collection<Article>) {
        for(item in items) {
            update(item)
        }
    }

    @Query("select exists(select 1 from article limit 1)")
    fun haveItems(): Boolean
}