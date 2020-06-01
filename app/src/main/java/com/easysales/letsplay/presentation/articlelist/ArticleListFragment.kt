package com.easysales.letsplay.presentation.articlelist

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.easysales.letsplay.R
import com.easysales.letsplay.infrastructure.App
import com.easysales.letsplay.presentation.BottomOffsetDecoration
import com.easysales.letsplay.presentation.MainActivity
import com.easysales.letsplay.presentation.core.BaseMVVMFragment
import com.easysales.letsplay.presentation.core.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_article_list.*
import javax.inject.Inject


class ArticleListFragment : BaseMVVMFragment<ArticleListVM>() {

    @Inject
    lateinit var viewModelViewModelFactory: ViewModelFactory
    private  lateinit var viewModel: ArticleListVM
    private lateinit var adapter: ArticleListAdapter

    override fun getVM(): ArticleListVM {
        return viewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_article_list

    override fun initialize() {
        App.getDI().addArticlesComponent().inject(this)
        viewModel = ViewModelProviders.of(this, viewModelViewModelFactory).get(ArticleListVM::class.java)
        super.initialize()
        setBar()

        this.adapter = ArticleListAdapter()
        addDisposable(adapter.itemClicks.subscribe(
            { viewModel.onArticleClick(it.articleId) },
            this::handleException
        ))

        rvArticles.layoutManager = LinearLayoutManager(context)
        rvArticles.addItemDecoration(BottomOffsetDecoration(resources.getDimension(R.dimen.recycleview_bottom_offset).toInt()))
        rvArticles.adapter = adapter
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            viewModel.loadArticles()
        }

        addDisposable(viewModel.viewDataLoaded().subscribe(
            { onViewDataChanged(it) },
            this::handleException
        ))

        addDisposable(viewModel.showArticle().subscribe(
            { showArticleView(it.articleId) },
            this::handleException
        ))

        viewModel.start()
        viewModel.loadArticles()
    }

    private fun showArticleView(articleId: String) {
        (activity as MainActivity).showArticleView(articleId)
    }

    private fun onViewDataChanged(viewDto: ViewDto) {
        try {
            adapter.reload(viewDto.items)
        } catch (e: Throwable) {
            this.handleException(e)
        }
    }

    private fun setBar() {
        getBaseActivity().supportActionBar!!.hide()
    }

}