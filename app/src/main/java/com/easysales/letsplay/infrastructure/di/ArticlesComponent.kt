package com.easysales.letsplay.infrastructure.di

import androidx.lifecycle.ViewModel
import com.easysales.letsplay.presentation.article.ArticleFragment
import com.easysales.letsplay.presentation.article.ArticleVM
import com.easysales.letsplay.presentation.articlelist.ArticleListFragment
import com.easysales.letsplay.presentation.articlelist.ArticleListVM
import com.easysales.letsplay.presentation.core.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent(modules = [ArticleModule::class])
interface ArticlesComponent {
    fun inject(articleListFragment: ArticleListFragment)
    fun inject(articleListFragment: ArticleFragment)
}

@Module
abstract class ArticleModule {
    @Binds
    @IntoMap
    @ViewModelKey(ArticleListVM::class)
    abstract fun bindArticleListVM(articleListVM: ArticleListVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleVM::class)
    abstract fun bindArticleVM(articleListVM: ArticleVM): ViewModel
}