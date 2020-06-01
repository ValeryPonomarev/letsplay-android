package com.easysales.letsplay.presentation

import com.easysales.letsplay.presentation.core.BaseView
import io.reactivex.Observable

interface MainContract {
    interface View : BaseView {
        fun showTutorialActivity()
        fun showFavoriteView()
        fun showHelpView()
        fun showArticlesView()
        fun showArticleView(articleId: String)

        var navigationClicks: Observable<NavigationItem>
    }
}

enum class NavigationItem {
    GAMES, HELP, FAVORITES
}