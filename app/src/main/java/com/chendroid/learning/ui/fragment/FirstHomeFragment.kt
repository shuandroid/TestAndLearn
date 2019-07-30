package com.chendroid.learning.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseFragment
import com.chendroid.learning.bean.BaseDatas
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import com.chendroid.learning.ui.holder.BannerHolder
import com.chendroid.learning.ui.holder.HomeListBanner
import com.chendroid.learning.ui.presenter.FirstHomeFragmentPresenterImpl
import com.chendroid.learning.ui.view.CollectArticleView
import com.chendroid.learning.ui.view.FirstHomeFragmentView
import com.chendroid.learning.widget.view.BannerRecyclerView
import com.zhihu.android.sugaradapter.SugarAdapter
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.fragment_first_home_layout.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import toast
import java.util.*

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/18
 */
class FirstHomeFragment : BaseFragment(), FirstHomeFragmentView, CollectArticleView {

    companion object {
        private const val BANNER_TIME = 3000L
    }

    private var mainView: View? = null

    private val bannerList: MutableList<HomeBanner.BannerItemData> = mutableListOf()

    // 用来装载 home 请求的数据
    private var homeResult: HomeListResponse? = null
    // 真正用来存在文章列表需要的数据
    private val articleList: MutableList<BaseDatas> = mutableListOf()

    private var holderBuilder: SugarAdapter.Builder = SugarAdapter.Builder.with(bannerList)

    private var listHolderBuilder: SugarAdapter.Builder = SugarAdapter.Builder.with(articleList)

    private val bannerDatas = mutableListOf<HomeBanner.BannerItemData>()

    private var bannerAdapter: SugarAdapter? = null

    private var homeListAdapter: SugarAdapter? = null

    private var bannerSwitchJob: Job? = null

    private var bannerRecyclerView: BannerRecyclerView? = null

    private var homeListRecyclerView: RecyclerView? = null

    // 当前 banner 的位置
    private var currentIndex = 0

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    private val verticalLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val homeFragmentPresenter: FirstHomeFragmentPresenterImpl by lazy {
        FirstHomeFragmentPresenterImpl(this, this)
    }

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
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    currentIndex = linearLayoutManager.findFirstVisibleItemPosition()
                    startSwitchJob()
                }
            }
        }
    }

    // 取消网络请求
    override fun cancelRequest() {
        homeFragmentPresenter.cancelRequest()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mainView = inflater.inflate(R.layout.fragment_first_home_layout, container, false)
        bannerRecyclerView = mainView?.findViewById(R.id.home_banner_recycler_view)
        homeListRecyclerView = mainView?.findViewById(R.id.home_recycler_view)
        return mainView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelSwitchJob()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 刷新
        home_swipe_refresh.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }


        bannerRecyclerView?.run {

            layoutManager = linearLayoutManager
            bannerPagerSnap.attachToRecyclerView(bannerRecyclerView)
            // 事件拦截
            requestDisallowInterceptTouchEvent(true)
            addOnScrollListener(onScrollListener)
        }

        holderBuilder.run {
            holderBuilder.add(BannerHolder::class.java)
        }

        bannerAdapter.run {

            bannerAdapter = holderBuilder.build()
            bannerRecyclerView?.adapter = bannerAdapter
        }

        // 有关文章的变量初始化
        listHolderBuilder.run {
            listHolderBuilder.add(HomeListBanner::class.java)
        }

        homeListAdapter.run {
            homeListAdapter = listHolderBuilder.build()
        }

        homeListRecyclerView?.run {
            layoutManager = verticalLayoutManager
            adapter = homeListAdapter
        }

        homeFragmentPresenter.getBanner()
        homeFragmentPresenter.getHomeList()
    }


    private fun refreshData() {

        home_swipe_refresh.isRefreshing = true

        cancelSwitchJob()

        homeFragmentPresenter.getBanner()
        homeFragmentPresenter.getHomeList()

        home_swipe_refresh.isRefreshing = false
    }

    private fun startSwitchJob() = bannerSwitchJob?.run {
        if (!isActive) {
            bannerSwitchJob = getBannerSwitchJob().apply { start() }
        }
    } ?: let {
        bannerSwitchJob = getBannerSwitchJob().apply { start() }
    }

    private fun cancelSwitchJob() = bannerSwitchJob?.run {

        if (isActive) {
            cancel()
        }
    }

    private fun getBannerSwitchJob() = launch {
        repeat(Int.MAX_VALUE) {
            if (bannerDatas.size == 0) {
                return@launch
            }

            delay(BANNER_TIME)
            currentIndex++
            val index = currentIndex % bannerDatas.size

            bannerRecyclerView?.smoothScrollToPosition(index)
            currentIndex = index
        }
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        // 全部的请求结果
        homeResult = result

        // 有关文章部分数据
        result.data.datas?.let {
            articleList.addAll(it)

            homeListAdapter?.notifyDataSetChanged()
        }

        home_swipe_refresh.isRefreshing = false
    }

    override fun getHomeListFailed(errorMessage: String?) {

    }

    override fun getHomeListZero() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeListSmall(result: HomeListResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBannerSuccess(result: HomeBanner) {
        Log.e("zc_test", "firstHomeFragment getBannerSuccess()")

        result.data?.let {
            bannerList.addAll(it)
            bannerDatas.addAll(it)
            startSwitchJob()
            bannerAdapter?.notifyDataSetChanged()

            Log.e("zc_test", "firstHomeFragment getBannerSuccess() it is $it")
        }

        home_swipe_refresh.isRefreshing = false
    }

    override fun getBannerFailed(errorMessage: String?) {
        Log.e("zc_test", "firstHomeFragment getBannerFailed()")

        errorMessage?.let {
            activity?.toast(it)
        } ?: let {
            activity?.toast("拉去失败了，。。。。")
        }

        home_swipe_refresh.isRefreshing = false
    }

    override fun getBannerZero() {
        Log.e("zc_test", "firstHomeFragment getBannerZero()")
        home_swipe_refresh.isRefreshing = false
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}