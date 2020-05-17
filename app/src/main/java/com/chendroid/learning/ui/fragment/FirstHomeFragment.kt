package com.chendroid.learning.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseFragment
import com.chendroid.learning.bean.BaseDatas
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.ui.holder.BannerLayoutHolder
import com.chendroid.learning.ui.holder.EmptyBannerData
import com.chendroid.learning.ui.holder.EmptyHolder
import com.chendroid.learning.ui.holder.HomeListItemHolder
import com.chendroid.learning.ui.holder.data.AllBannerDataWrapper
import com.chendroid.learning.ui.holder.data.EmptyData
import com.chendroid.learning.vm.CollectArticleViewModel
import com.chendroid.learning.vm.FirstHomeViewModel
import com.chendroid.learning.widget.view.CustomItemDecoration
import com.zhihu.android.sugaradapter.SugarAdapter
import kotlinx.android.synthetic.main.fragment_first_home_layout.*

/**
 * @intro 首页， wanandroid 网页的列表页和 banner 页
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/18
 */
class FirstHomeFragment : BaseFragment(), HomeListItemHolder.HomeItemListener {

    companion object {
        private const val BANNER_TIME = 3000L
    }

    // 真正用来存在文章列表需要的数据
    private val articleList: MutableList<Any> = mutableListOf()


    private var listHolderBuilder: SugarAdapter.Builder = SugarAdapter.Builder.with(articleList)

    private val homeListAdapter by lazy {
        listHolderBuilder.build()
    }

    private lateinit var homeListRecyclerView: RecyclerView

    private lateinit var firstHomeVM: FirstHomeViewModel

    private val collectArticleVM: CollectArticleViewModel by lazy {
        //todo 为什么第一种方案实现不了
//        ViewModelProvider(this).get(CollectArticleViewModel::class.java)
        ViewModelProviders.of(this).get(CollectArticleViewModel::class.java)
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    // 文章列表的滚动监听
    private val articleListScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            this@FirstHomeFragment.onScrolled(recyclerView, dx, dy)
        }
    }

    init {
        listHolderBuilder
                .add(HomeListItemHolder::class.java) { holder ->
                    holder.homeItemListener = this
                }
                .add(EmptyHolder::class.java)
                .add(BannerLayoutHolder::class.java)
    }


    // 取消网络请求
    override fun cancelRequest() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mainView = inflater.inflate(R.layout.fragment_first_home_layout, container, false)
        homeListRecyclerView = mainView.findViewById(R.id.home_recycler_view)!!
        mainView.post { }
        return mainView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 刷新
        home_swipe_refresh.apply {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        homeListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = homeListAdapter
            addOnScrollListener(articleListScrollListener)
            // 设置 recyclerView item 分割条
            addItemDecoration(CustomItemDecoration.with(context))
        }

        bindViewModel()

        loadData()
    }

    /**
     * 初始化 VM
     */
    private fun bindViewModel() {

        //todo 为什么会失败呢
//        firstHomeVM = ViewModelProvider(this).get(FirstHomeViewModel::class.java)
        firstHomeVM = ViewModelProviders.of(this).get(FirstHomeViewModel::class.java)

        // banner 成功的 监听
        val bannerLDObserver = Observer<List<HomeBanner.BannerItemData>> {

            val bannerDataWrapper = AllBannerDataWrapper(it, null, "来自网络数据源")
            articleList.add(0, bannerDataWrapper)

            home_swipe_refresh.isRefreshing = false
        }

        val bannerEmptyLDObserver = Observer<EmptyBannerData> {
            // todo
            home_swipe_refresh.isRefreshing = false
        }

        val articleListObserver = Observer<List<BaseDatas>> {
            articleList.addAll(it)
            Log.i("zc_test", "FirstHome Fragment getHomeListSuccess() datas is not null ")
            homeListAdapter.notifyDataSetChanged()
            home_swipe_refresh.isRefreshing = false
        }

        val articleEmptyAndErrorObserver = Observer<EmptyData> {
            Log.i("zc_test", "FirstHome Fragment 空界面 ")
            articleList.add(it)
            homeListAdapter.notifyDataSetChanged()
            home_swipe_refresh.isRefreshing = false
        }

        firstHomeVM.bannerUILD.observe(viewLifecycleOwner, bannerLDObserver)
        firstHomeVM.bannerEmptyLD.observe(viewLifecycleOwner, bannerEmptyLDObserver)
        firstHomeVM.articleLD.observe(viewLifecycleOwner, articleListObserver)
        firstHomeVM.articleEmptyLD.observe(viewLifecycleOwner, articleEmptyAndErrorObserver)
    }

    /**
     * 首次进入获取数据
     */
    private fun loadData() {
        home_swipe_refresh.isRefreshing = true
        // 清楚数据
        articleList.clear()
        firstHomeVM.run {
            getBannerData()
            getArticleList()
        }
    }

    /**
     * 刷新数据
     */
    private fun refreshData() {
        loadData()
    }

    // 滚动时会调用该方法，去判断是否需要加载新的数据 loadMore
    private fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        if (dy == 0) {
            return
        }

        if (isScrollingTriggerLoadingMore(recyclerView) && canLoadMore()) {
            recyclerView.post { loadMoreData() }
        }
    }

    /**
     * 只对 LinearLayoutManager 生效，其他 LayoutManager 请重写此方法
     */
    private fun isScrollingTriggerLoadingMore(recyclerView: RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager

        layoutManager?.run {
            val totalItemCount = itemCount
            val lastVisibleItemPosition = if (this is LinearLayoutManager) {
                findLastVisibleItemPosition()
            } else {
                return false
            }

            return totalItemCount > 0 && totalItemCount - lastVisibleItemPosition - 1 <= 5
        }

        return false
    }

    /**
     * 判断是否可以加载数据
     * 防止重复拉取数据
     */
    private fun canLoadMore(): Boolean {
        return !firstHomeVM.isLoadingArticle
    }

    /**
     * 获取更多数据
     */
    private fun loadMoreData() {
        val page = homeListAdapter.list.size / 20 + 1
        firstHomeVM.getArticleList(page)
    }

    override fun onCollectViewClicked(articleId: Int, originId: Int, isCollected: Boolean, handleResult: (Boolean) -> Unit) {
        if (isCollected) {
            // 如果已经收藏过，则是取消收藏
            collectArticleVM.unCollectArticle(articleId, originId, handleResult)
        } else {
            // 未收藏过，则需要调用收藏接口
//            firstHomeVM.collectArticle(articleId, handleResult)
            collectArticleVM.collectArticle(articleId, handleResult)
        }
    }
}