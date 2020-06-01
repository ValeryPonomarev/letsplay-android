package com.easysales.letsplay.presentation.articlelist

import com.easysales.letsplay.data.SchedulerProvider
import com.easysales.letsplay.domain.article.ArticleService
import com.easysales.letsplay.presentation.core.BaseVM
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ArticleListVM @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val articleService: ArticleService
) : BaseVM() {
    private val viewData = BehaviorSubject.create<ViewDto>()
    private val showArticle = BehaviorSubject.create<ArticleClickEvent>()

    override fun start() {

    }

    fun viewDataLoaded(): Observable<ViewDto> = viewData
    fun showArticle(): Observable<ArticleClickEvent> = showArticle

    fun loadArticles() {
        addDisposable(articleService.observeArticles()
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { showLoader() }
            .doAfterNext { hideLoader() }
            .doOnError { hideLoader() }
            .subscribe(
                { items -> viewData.onNext(ViewDto(items = items.map { it.toViewDto() })) },
                this::handleException
            ))
    }

    fun onArticleClick(articleId: String) {
        showArticle.onNext(ArticleClickEvent(articleId))
    }
}