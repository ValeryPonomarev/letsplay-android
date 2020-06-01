package com.easysales.letsplay.presentation

import com.easysales.letsplay.domain.CommonService
import com.easysales.letsplay.infrastructure.App
import com.easysales.letsplay.presentation.core.BasePresenter
import com.easysales.letsplay.utils.LogUtils
import com.easysales.letsplay.utils.Settings
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainPresenter() : BasePresenter<MainContract.View>() {

    @Inject lateinit var commonService: CommonService

    override fun start(view: MainContract.View) {
        super.start(view)
        App.getDI().appComponent!!.inject(this)

        if(commonService.isFirstLaunch()) {
            getView().showTutorialActivity()
            return
        }

        addDisposable(getView().navigationClicks
            .debounce(Settings.UI_DEBOUNCE, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .flatMapCompletable { Completable.fromCallable {
                when(it) {
                    NavigationItem.FAVORITES -> getView().showFavoriteView()
                    NavigationItem.HELP -> getView().showHelpView()
                    NavigationItem.GAMES -> getView().showArticlesView()
                    NavigationItem.LOGS -> getView().sendLogs()
                }
            }}
            .doOnError { processException(it) }.retry()
            .subscribe(
                { },
                { processException(it) }
            )
        )
    }
}