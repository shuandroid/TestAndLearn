package com.chendroid.learning.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseFragment
import com.chendroid.learning.bean.ArticleTagData
import com.chendroid.learning.ui.holder.ArticleTypeHolder
import com.chendroid.learning.ui.holder.data.ArticleTagDataWrapper
import com.chendroid.learning.vm.MoreArticleTagViewModel
import com.chendroid.learning.widget.view.CustomItemDecoration
import com.zhihu.android.sugaradapter.SugarAdapter
import kotlinx.android.synthetic.main.fragment_more_type_layout.*

/**
 * @intro  更多，文章标签部分
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/18
 */
class MoreArticleTagFragment : BaseFragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var recyclerView: RecyclerView

    // 数据源
    private lateinit var articleTagViewModel: MoreArticleTagViewModel

    //
    // 文章类型列表
    private val articleTypeList: MutableList<ArticleTagDataWrapper> = mutableListOf()
    // sugarAdapter builder
    private val typeListBuilder = SugarAdapter.Builder.with(articleTypeList)

    private lateinit var articleTypeAdapter: SugarAdapter


    // 文章种类的滚动监听， 为了加载其余标签.
    private val articleListScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    override fun cancelRequest() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more_type_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        setupSwipeRefreshLayout()
        setupRecyclerView()

        // 首次刷新数据
        refreshData()
    }

    private fun setupRecyclerView() {
        recyclerView = article_type_recycler

        val verticalLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = verticalLayoutManager

        typeListBuilder.add(ArticleTypeHolder::class.java)

        articleTypeAdapter = typeListBuilder.build()

        recyclerView.adapter = articleTypeAdapter
        recyclerView.addItemDecoration(CustomItemDecoration.with(context!!))

        recyclerView.addOnScrollListener(articleListScrollListener)
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = type_swipe_refresh
        swipeRefreshLayout.setOnRefreshListener { refreshData() }
    }

    /**
     * 设置 viewModel
     */
    private fun setupViewModel() {
        articleTagViewModel = ViewModelProviders.of(this).get(MoreArticleTagViewModel::class.java)

        val loadArticleTypeSuccess = Observer<List<ArticleTagData>> {
            // 当收到 postValue 变化时，会回调此处
            onRefreshCompleted(it)
        }

        articleTagViewModel.onLoadDataSuccess().observe(this, loadArticleTypeSuccess)

    }

    /**
     * 刷新数据
     */
    private fun refreshData() {
        // 刷新
        swipeRefreshLayout.isRefreshing = true
        articleTagViewModel.refreshData()
    }

    private fun onRefreshCompleted(articleTagDataList: List<ArticleTagData>?) {

        if (articleTagDataList == null) {
            Log.i("zc_test", "MoreArticleTagFragment + onRefreshCompleted 没有获取到数据")
            swipeRefreshLayout.isRefreshing = false
            return
        }

        articleTagDataList.map {
            ArticleTagDataWrapper(it)
        }.run {
            articleTypeList.addAll(this)

            articleTypeAdapter.notifyDataSetChanged()
        }

        swipeRefreshLayout.isRefreshing = false

    }

}