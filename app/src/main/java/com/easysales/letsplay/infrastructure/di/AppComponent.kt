package com.easysales.letsplay.infrastructure.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.easysales.letsplay.data.SchedulerProvider
import com.easysales.letsplay.data.article.ArticleRepository
import com.easysales.letsplay.data.article.ArticleRepositoryImpl
import com.easysales.letsplay.data.storage.IPreferencesLocalStorage
import com.easysales.letsplay.domain.CommonService
import com.easysales.letsplay.domain.article.ArticleService
import com.easysales.letsplay.domain.article.ArticleServiceImpl
import com.easysales.letsplay.presentation.MainActivity
import com.easysales.letsplay.presentation.MainPresenter
import com.easysales.letsplay.presentation.core.ViewModelFactory
import com.easysales.letsplay.utils.AnalyticUtils
import com.easysales.letsplay.utils.IAnalyticUtils
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
interface AppComponent {
    fun addTutorialComponent(tutorialModule: TutorialModule) : TutorialComponent
    fun addArticlesComponent(): ArticlesComponent

    fun inject(mainActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)
}

@Module
open class AppModule(private val context: Context) {
    @Provides @Singleton fun provideAppContext(): Context = context
    @Provides @Singleton open fun provideSchedulerProvider(): SchedulerProvider = object :
        SchedulerProvider {
        override fun io(): Scheduler = Schedulers.io()
        override fun computation(): Scheduler = Schedulers.computation()
        override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    }
    @Provides @Singleton open fun provideAnalyticUtils(context: Context): IAnalyticUtils = AnalyticUtils(context)
    @Provides @Singleton fun provideMainPresenter(): MainPresenter = MainPresenter()
}

@Module
class ServiceModule {
    @Provides @Singleton fun provideArticleService(articleRepository: ArticleRepository): ArticleService = ArticleServiceImpl(articleRepository)
    @Provides @Singleton fun provideCommonService(preferencesLocalStorage: IPreferencesLocalStorage): CommonService = com.easysales.letsplay.data.service.impl.CommonServiceImpl(preferencesLocalStorage)
}

@Module
abstract class RepositoryModule {
    @Binds abstract fun bindArticleRepository(articleRepositoryImpl: ArticleRepositoryImpl): ArticleRepository
}

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

