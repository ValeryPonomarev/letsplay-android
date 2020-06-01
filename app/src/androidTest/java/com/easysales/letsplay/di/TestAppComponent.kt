package com.easysales.letsplay.di

import android.content.Context
import androidx.room.Room
import com.easysales.letsplay.BaseTest
import com.easysales.letsplay.data.SchedulerProvider
import com.easysales.letsplay.data.db.Database
import com.easysales.letsplay.data.http.HttpClient
import com.easysales.letsplay.data.storage.IPreferencesLocalStorage
import com.easysales.letsplay.data.storage.TestPreferencesLocalStorage
import com.easysales.letsplay.infrastructure.di.*
import com.easysales.letsplay.tests.ArticleTest
import dagger.Component
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.mockito.Mockito
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class,
    ServiceModule::class,
    RepositoryModule::class,
    NetworkModule::class,
    StorageModule::class
])
interface TestInstAppComponent: AppComponent {
    fun addTestArticlesComponent(): TestArticlesComponent
    fun inject(baseTest: BaseTest)
    fun inject(test: ArticleTest)
}

class TestAppModule(context: Context): AppModule(context) {
    override fun provideSchedulerProvider(): SchedulerProvider = object :
        SchedulerProvider {
        override fun io(): Scheduler = Schedulers.trampoline()
        override fun computation(): Scheduler = Schedulers.trampoline()
        override fun ui(): Scheduler = Schedulers.trampoline()
    }
}

class TestNetworkModule: NetworkModule("https://www.google.com") {
    override fun provideHttpClient(retrofit: Retrofit): HttpClient = Mockito.mock(HttpClient::class.java)
}

class TestStorageModule(private val context: Context): StorageModule(context) {
    override fun providePreferencesLocalStorage(): IPreferencesLocalStorage {
        return TestPreferencesLocalStorage()
    }

    override fun provideDbLocalStorage(): Database {
        return Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .build()
    }
}