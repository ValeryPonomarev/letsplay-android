package com.easysales.letsplay.data.article

import androidx.room.*
import com.easysales.letsplay.data.db.BaseDao
import io.reactivex.Maybe

@Entity(
    tableName = "favorite",
    indices = [Index(value = ["articleApiId"], unique = true)]
)
data class Favorite(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "articleApiId") val articleApiId: Long
)

@Dao
interface FavoriteDao: BaseDao<Favorite> {
    @Query("delete from favorite")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Favorite>)

    @Query("select * from favorite where articleApiId = :articleApiId")
    fun getByArticleApiId(articleApiId: Long): Maybe<Favorite>
}