package com.easysales.letsplay.presentation.tutorial

import com.easysales.letsplay.data.dto.TutorialDto
import com.easysales.letsplay.presentation.core.BaseView
import io.reactivex.Observable

interface TutorialView : BaseView {
    fun showTutorials(tutorials: List<TutorialDto>)
    fun showPlayListView()
    fun showNextPage()

    var onNextClick : Observable<NextClickEvent>

    class NextClickEvent(public val isLastPage : Boolean)
}