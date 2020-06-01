package com.easysales.letsplay.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.easysales.letsplay.R
import com.easysales.letsplay.data.dto.TutorialDto
import com.easysales.letsplay.infrastructure.App
import com.easysales.letsplay.presentation.MainActivity
import com.easysales.letsplay.presentation.core.BaseMvpFragment
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.content_tutorial.*
import javax.inject.Inject

class HelpFragment : BaseMvpFragment<TutorialPresenter, TutorialView>(), TutorialView {

    @Inject
    lateinit var tutorialPresenter: TutorialPresenter

    lateinit var adapter: TutorialAdapter
    override lateinit var onNextClick: Observable<TutorialView.NextClickEvent>

    override fun showTutorials(tutorials: List<TutorialDto>) {
        adapter.addAll(tutorials)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getDI().tutorialComponent!!.inject(this)
    }

    override fun initialize() {
        hideNextButton()
        adapter = TutorialAdapter(activity!!.supportFragmentManager, ArrayList())

        tutorialViewPager.adapter = adapter

        onNextClick = nextButton.clicks()
            .mergeWith(startButton.clicks())
            .map { TutorialView.NextClickEvent(adapter.isLast(tutorialViewPager.currentItem)) }

        setBar()
        getPresenter().start(this.getBaseView())
        getPresenter().loadData()
    }

    override fun getPresenter(): TutorialPresenter {
        return tutorialPresenter
    }

    override fun getLayoutId(): Int = R.layout.fragment_tutorial

    override fun showPlayListView() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity!!.finish()
    }

    override fun showNextPage() {
        tutorialViewPager.currentItem = tutorialViewPager.currentItem + 1
    }

    private fun hideNextButton() {
        nextButton.visibility = View.GONE
        startButton.visibility = View.GONE
    }

    private fun setBar() {
        getBaseActivity().supportActionBar!!.show()
        getBaseActivity().setToolbarTitle(R.string.tutorial_title_help)
        getBaseActivity().supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }
}