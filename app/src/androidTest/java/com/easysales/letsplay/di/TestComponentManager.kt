package com.easysales.letsplay.di

import androidx.test.platform.app.InstrumentationRegistry
import com.easysales.letsplay.infrastructure.di.ServiceModule
import com.easysales.letsplay.infrastructure.di.TutorialComponent


class TestComponentManager() {

    val appComponent: TestInstAppComponent = DaggerTestInstAppComponent.builder()
        .appModule(TestAppModule(InstrumentationRegistry.getInstrumentation().context))
        .networkModule(TestNetworkModule())
        .serviceModule(ServiceModule())
        .storageModule(TestStorageModule(InstrumentationRegistry.getInstrumentation().context))
        .build()

    var tutorialComponent: TutorialComponent? = null
        get() = field

    var articlesComponent: TestArticlesComponent? = null
        get() {
            if (field == null) {
                println("Create article component")
                field = appComponent.addTestArticlesComponent()
            }
            return field!!
        }
}