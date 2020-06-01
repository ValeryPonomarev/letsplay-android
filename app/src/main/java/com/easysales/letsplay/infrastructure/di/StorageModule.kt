package com.easysales.letsplay.infrastructure.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.easysales.letsplay.data.db.Database
import com.easysales.letsplay.data.storage.AssetsStorage
import com.easysales.letsplay.data.storage.IPreferencesLocalStorage
import com.easysales.letsplay.data.storage.PreferencesLocalStorage
import com.easysales.letsplay.utils.LogUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class StorageModule(private val context: Context) {
    @Provides @Singleton open fun provideDbLocalStorage(): Database =
        Room
            .databaseBuilder(context, Database::class.java, Database.APP_DB_NAME)
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    LogUtils.iFile(LogUtils.LOG_DATA, "populate database")
//                    db.execSQL("delete from goodCancellationReason;")
//                    db.execSQL("insert into goodCancellationReason(id, title) values \n" +
//                            "('1', 'Брак'),\n" +
//                            "('2', 'Отказ');")
                }
            })
            .build()

    @Provides @Singleton open fun providePreferencesLocalStorage(): IPreferencesLocalStorage = PreferencesLocalStorage(context)
    @Provides @Singleton open fun provideAssetsStorage(): AssetsStorage = AssetsStorage(context)
}