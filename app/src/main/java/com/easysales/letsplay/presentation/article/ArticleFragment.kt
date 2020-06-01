package com.easysales.letsplay.presentation.article

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.easysales.letsplay.R
import com.easysales.letsplay.infrastructure.App
import com.easysales.letsplay.presentation.core.BaseMVVMFragment
import com.easysales.letsplay.presentation.core.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_article.*
import javax.inject.Inject


class ArticleFragment : BaseMVVMFragment<ArticleVM>() {

    @Inject
    lateinit var viewModelViewModelFactory: ViewModelFactory
    private  lateinit var viewModel: ArticleVM

    override fun getVM(): ArticleVM {
        return viewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_article

    override fun initialize() {
        App.getDI().addArticlesComponent().inject(this)
        viewModel = ViewModelProviders.of(this, viewModelViewModelFactory).get(ArticleVM::class.java)
        super.initialize()
        setBar()
        initWebView()

        addDisposable(viewModel.viewDataLoaded().subscribe(
            { onViewDataChanged(it) },
            this::handleException
        ))

        val args = arguments!!.getSerializable(ARG) as Args

        viewModel.start()
        viewModel.loadArticle(args.articleId)
    }

    private fun initWebView() {
//        val settings = wvHtmlContent.settings
//        settings.loadWithOverviewMode = true
//        settings.useWideViewPort = true
    }

    private fun onViewDataChanged(viewDto: ViewDto) {
        try {
            wvHtmlContent.loadData(viewDto.contentHtml, "text/html; charset=utf-8", "UTF-8");
        } catch (e: Throwable) {
            this.handleException(e)
        }
    }

    private fun setBar() {
        getBaseActivity().supportActionBar!!.hide()
    }

    companion object {
        private const val ARG = "ARG"

        fun newInstance(args: Args) : ArticleFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARG, args)

            val fragment = ArticleFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}