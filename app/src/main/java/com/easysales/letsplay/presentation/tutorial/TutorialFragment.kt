package com.easysales.letsplay.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.easysales.letsplay.R
import com.easysales.letsplay.data.dto.TutorialDto
import com.easysales.letsplay.infrastructure.App
import com.easysales.letsplay.presentation.MainActivity
import com.easysales.letsplay.presentation.core.BaseMvpFragment
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.content_tutorial.*
import javax.inject.Inject


class TutorialFragment : BaseMvpFragment<TutorialPresenter, TutorialView>(), TutorialView {

    var isHelp = false

    @Inject
    lateinit var tutorialPresenter: TutorialPresenter

    lateinit var adapter: TutorialAdapter
    override lateinit var onNextClick: Observable<TutorialView.NextClickEvent>

    companion object {
        private const val ARG_IS_HELP = "is_help"

        fun newInstance(isHelp: Boolean) : TutorialFragment {
            val args = Bundle()
            args.putBoolean(ARG_IS_HELP, isHelp)

            val tutorialFragment = TutorialFragment()
            tutorialFragment.arguments = args
            return tutorialFragment
        }
    }

    override fun showTutorials(tutorials: List<TutorialDto>) {
        adapter.addAll(tutorials)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getDI().tutorialComponent!!.inject(this)
    }

    override fun initialize() {
        adapter = TutorialAdapter(activity!!.supportFragmentManager, ArrayList())
        isHelp = arguments!!.getBoolean(ARG_IS_HELP)
        if(isHelp) {
            hideNextButton()
        }

        tutorialViewPager.adapter = adapter
        tutorialViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                if(tutorialViewPager.adapter == null) {
                    return
                }

                if(isHelp) return

                if (i == tutorialViewPager.adapter!!.count - 1) {
                    nextButton.visibility = View.GONE
                    startButton.visibility = View.VISIBLE
                } else {
                    nextButton.visibility = View.VISIBLE
                    startButton.visibility = View.GONE
                }
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

        onNextClick = nextButton.clicks()
            .mergeWith(startButton.clicks())
            .map { TutorialView.NextClickEvent(adapter.isLast(tutorialViewPager.currentItem)) }

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
}