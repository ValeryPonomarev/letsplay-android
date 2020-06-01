package com.easysales.letsplay.data.article

import androidx.room.*
import com.easysales.letsplay.data.db.BaseDao
import com.easysales.letsplay.data.db.Database
import io.reactivex.Flowable

@Entity(
    tableName = "category",
    indices = [Index(value = ["apiId"], unique = true)]
)
data class Category(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "apiId") val apiId: Long,
    @ColumnInfo(name = "title") val title: String
)

fun CategoryResponse.toDb() : Category {
    val id = Database.generateId()
    return Category(
        id,
        this.apiId,
        this.title
    )
}

@Dao
interface CategoryDao: BaseDao<Category> {
    @Query("select * from category")
    fun observeAll(): Flowable<List<Category>>

    @Query("delete from category")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: Collection<Category>)
}