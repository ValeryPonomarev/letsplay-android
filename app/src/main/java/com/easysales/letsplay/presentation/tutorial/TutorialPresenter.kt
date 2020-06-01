package com.easysales.letsplay.presentation.tutorial

import com.easysales.letsplay.domain.CommonService
import com.easysales.letsplay.domain.tutorial.TutorialService
import com.easysales.letsplay.infrastructure.App
import com.easysales.letsplay.presentation.core.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TutorialPresenter() : BasePresenter<TutorialView>() {
    @Inject
    lateinit var tutorialService : TutorialService

    @Inject
    lateinit var commonService: CommonService

    override fun start(view: TutorialView) {
        super.start(view)
        App.getDI().tutorialComponent!!.inject(this)

        addDisposable(tutorialService.getTutorials()
            .toList()
            .subscribe(
                { data -> getView().showTutorials(data) },
                { throwable -> processException(throwable) }
            ))

        addDisposable(getView()
            .onNextClick
            .debounce(50, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe(
                { event -> if (event.isLastPage) {
                    commonService.doFirstLaunch()
                    getView().showPlayListView()
                } else  {
                    getView().showNextPage()
                }
                },
                { throwable -> processException(throwable) }
            ))
    }

    fun loadData() {

    }
}