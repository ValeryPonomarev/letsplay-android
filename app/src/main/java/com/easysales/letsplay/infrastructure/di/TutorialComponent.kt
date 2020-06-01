package com.easysales.letsplay.infrastructure.di

import com.easysales.letsplay.domain.tutorial.TutorialService
import com.easysales.letsplay.domain.tutorial.TutorialServiceImpl
import com.easysales.letsplay.presentation.tutorial.HelpFragment
import com.easysales.letsplay.presentation.tutorial.TutorialFragment
import com.easysales.letsplay.presentation.tutorial.TutorialPresenter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@TutorialScope
@Subcomponent(modules = [TutorialModule::class])
interface TutorialComponent {
    fun inject(tutorialPresenter: TutorialPresenter)
    fun inject(tutorialFragment: TutorialFragment)
    fun inject(helpFragment: HelpFragment)
}

@Scope
@Retention
annotation class TutorialScope

@Module
class TutorialModule {
    @Provides @TutorialScope fun provideTutorialService() : TutorialService =
        TutorialServiceImpl()
    @Provides @TutorialScope fun provideTutorialPresenter() : TutorialPresenter = TutorialPresenter()
}