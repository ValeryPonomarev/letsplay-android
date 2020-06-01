package com.easysales.letsplay.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.easysales.letsplay.data.article.*
import java.util.*

@Database(entities = [
    Article::class,
    Category::class,
    ArticleCategory::class,
    Favorite::class
], version = 1
, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun categoryDao(): CategoryDao
    abstract fun articleCategoryDao(): ArticleCategoryDao
    abstract fun favoriteDao(): FavoriteDao

    companion object DATABASE {
        const val APP_DB_NAME = "app"
        const val CHANGE_DEBOUNCE = 300L
        const val MAX_ARGUMENTS = 500

        fun generateId() : String = UUID.randomUUID().toString()
    }
}