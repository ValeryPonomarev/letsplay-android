package com.easysales.letsplay.di

import com.easysales.letsplay.infrastructure.di.ArticleModule
import com.easysales.letsplay.infrastructure.di.ArticlesComponent
import dagger.Subcomponent

@Subcomponent(modules = [ArticleModule::class])
interface TestArticlesComponent: ArticlesComponent {
}