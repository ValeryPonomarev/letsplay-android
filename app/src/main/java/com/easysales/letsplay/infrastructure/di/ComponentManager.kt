package com.easysales.letsplay.infrastructure.di

import android.content.Context
import com.easysales.letsplay.BuildConfig
import com.easysales.letsplay.utils.LogUtils


class ComponentManager(context: Context) {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(context))
        .networkModule(NetworkModule(BuildConfig.API_URL))
        .storageModule(StorageModule(context))
        .serviceModule(ServiceModule())
        .build()

        get() = field
    var tutorialComponent: TutorialComponent? = null
        get() = field

    //region Tutorial
    fun addTutorialComponent(): TutorialComponent {
        if (tutorialComponent == null) {
            LogUtils.d(LogUtils.LOG_APP, "Create tutorial component")
            tutorialComponent = appComponent.addTutorialComponent(TutorialModule())
        }
        return tutorialComponent!!
    }

    fun clearTutorialComponent() {
        tutorialComponent = null
    }
    //endregion

    fun addArticlesComponent(): ArticlesComponent {
        LogUtils.d(LogUtils.LOG_APP, "Create articles component")
        return appComponent.addArticlesComponent()
    }
}