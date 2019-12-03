package com.chendroid.learning.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseFragment
import com.chendroid.learning.bean.BaseDatas
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.ui.holder.*
import com.chendroid.learning.ui.holder.data.EmptyData
import com.chendroid.learning.vm.FirstHomeViewModel
import com.chendroid.learning.widget.view.BannerRecyclerView
import com.chendroid.learning.widget.view.CustomItemDecoration
import com.zhihu.android.sugaradapter.SugarAdapter
import kotlinx.android.synthetic.main.fragment_first_home_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @intro 首页， wanandroid 网页的列表页和 banner 页
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/18
 */
class FirstHomeFragment : BaseFragment() {

    companion object {
        private const val BANNER_TIME = 3000L
    }

    // banner 的
    private val bannerList: MutableList<Any> = mutableListOf()

    // 真正用来存在文章列表需要的数据
    private val articleList: MutableList<Any> = mutableListOf()

    private var holderBuilder: SugarAdapter.Builder = SugarAdapter.Builder.with(bannerList)

    private var listHolderBuilder: SugarAdapter.Builder = SugarAdapter.Builder.with(articleList)

    private val bannerDataList = mutableListOf<HomeBanner.BannerItemData>()

    private val bannerAdapter by lazy {
        holderBuilder.build()
    }

    private val homeListAdapter by lazy {
        listHolderBuilder.build()
    }

    private var bannerSwitchJob: Job? = null

    private lateinit var bannerRecyclerView: BannerRecyclerView

    private lateinit var homeListRecyclerView: RecyclerView

    private lateinit var firstHomeVM: FirstHomeViewModel

    // 当前 banner 的位置
    private var currentIndex = 0

    /**
     * Banner PagerSnapHelper
     */
    private val bannerPagerSnap: PagerSnapHelper by lazy {
        PagerSnapHelper()
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    //当是 悬停状态 idle 时，需要开启自动切换开关
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        // RecyclerView 的滚动监听
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    currentIndex = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    startSwitchJob()
                }
            }
        }
    }

    // 文章列表的滚动监听
    private val articleListScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            this@FirstHomeFragment.onScrolled(recyclerView, dx, dy)
        }
    }

    init {
        // sugarBuilder 添加 SugarHolder
        holderBuilder.add(BannerHolder::class.java)
                .add(EmptyBannerHolder::class.java)
        listHolderBuilder.add(HomeListItemHolder::class.java)
                .add(EmptyHolder::class.java)
    }


    // 取消网络请求
    override fun cancelRequest() {

//        homeFragmentPresenter.cancelRequest()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mainView = inflater.inflate(R.layout.fragment_first_home_layout, container, false)
        bannerRecyclerView = mainView.findViewById(R.id.home_banner_recycler_view)!!
        homeListRecyclerView = mainView.findViewById(R.id.home_recycler_view)!!
        return mainView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelSwitchJob()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 刷新
        home_swipe_refresh.apply {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        bannerRecyclerView.apply {

            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            bannerPagerSnap.attachToRecyclerView(bannerRecyclerView)
            // 事件拦截
            requestDisallowInterceptTouchEvent(true)
            addOnScrollListener(onScrollListener)
            adapter = bannerAdapter
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
        firstHomeVM = ViewModelProviders.of(this).get(FirstHomeViewModel::class.java)

        // banner 成功的 监听
        val bannerLDObserver = Observer<List<HomeBanner.BannerItemData>> {
            bannerList.addAll(it)
            bannerDataList.addAll(it)
            startSwitchJob()
            bannerAdapter.notifyDataSetChanged()
            //
            home_swipe_refresh.isRefreshing = false
        }

        val bannerEmptyLDObserver = Observer<EmptyBannerData> {
            bannerList.add(it)
            bannerAdapter.notifyDataSetChanged()
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

        firstHomeVM.bannerUILD.observe(this, bannerLDObserver)
        firstHomeVM.bannerEmptyLD.observe(this, bannerEmptyLDObserver)
        firstHomeVM.articleLD.observe(this, articleListObserver)
        firstHomeVM.articleEmptyLD.observe(this, articleEmptyAndErrorObserver)
    }

    /**
     * 首次进入获取数据
     */
    private fun loadData() {
        cancelSwitchJob()
        home_swipe_refresh.isRefreshing = true
        // 清楚数据
        bannerList.clear()
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

    private fun startSwitchJob() = bannerSwitchJob?.run {
        if (!isActive) {
            bannerSwitchJob = getBannerSwitchJob().apply { start() }
        }
    } ?: let {
        bannerSwitchJob = getBannerSwitchJob().apply {
            start()
        }
    }

    private fun cancelSwitchJob() = bannerSwitchJob?.run {

        if (isActive) {
            cancel()
        }
    }

    private fun getBannerSwitchJob() = GlobalScope.launch {
        repeat(Int.MAX_VALUE) {
            if (bannerDataList.size == 0) {
                return@launch
            }

            delay(BANNER_TIME)
            currentIndex++
            val index = currentIndex % bannerDataList.size

            bannerRecyclerView.smoothScrollToPosition(index)
            currentIndex = index
        }
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
}