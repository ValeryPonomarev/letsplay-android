package com.easysales.letsplay.presentation.article

import com.easysales.letsplay.data.SchedulerProvider
import com.easysales.letsplay.domain.article.ArticleService
import com.easysales.letsplay.presentation.core.BaseVM
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ArticleVM @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val articleService: ArticleService
) : BaseVM() {
    private val viewData = BehaviorSubject.create<ViewDto>()

    override fun start() {

    }

    fun viewDataLoaded(): Observable<ViewDto> = viewData

    fun loadArticle(articleId: String) {
        addDisposable(articleService.getArticleHtml(articleId)
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { viewData.onNext(ViewDto(it)) },
                this::handleException
            ))
    }
}