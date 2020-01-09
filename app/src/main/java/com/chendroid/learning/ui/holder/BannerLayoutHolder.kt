package com.chendroid.learning.ui.holder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.chendroid.learning.R
import com.chendroid.learning.ui.holder.data.AllBannerDataWrapper
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarAdapter
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.holder_banner_cover_layout.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @intro 外层，承载 banner 布局的区域部分, 数据类为 AllBannerDataWrapper
 * @author zhaochen@ZhiHu Inc.
 * @since 2020-01-09
 */
@Layout(R.layout.holder_banner_cover_layout)
class BannerLayoutHolder(view: View) : SugarHolder<AllBannerDataWrapper>(view), View.OnClickListener {

    // banner 的
    private val bannerList: MutableList<Any> = mutableListOf()

    private var bannerHolderBuilder: SugarAdapter.Builder = SugarAdapter.Builder.with(bannerList)

    private val recyclerView by lazy {
        itemView.home_banner_recycler_view
    }

    private val bannerAdapter by lazy {
        bannerHolderBuilder.build()
    }

    // 自动轮播
    private var bannerSwitchJob: Job? = null

    /**
     * Banner PagerSnapHelper
     */
    private val bannerPagerSnap: PagerSnapHelper by lazy {
        PagerSnapHelper()
    }

    // 当前 banner 的位置
    private var currentIndex = 0

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

    init {
        itemView.setOnClickListener(this)
        initAdapter()
        initRecyclerView()
    }


    private fun initAdapter() {
        bannerHolderBuilder.add(EmptyBannerHolder::class.java)
                .add(BannerHolder::class.java)
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            bannerPagerSnap.attachToRecyclerView(this)
            requestDisallowInterceptTouchEvent(true)
            addOnScrollListener(onScrollListener)
            adapter = bannerAdapter
        }

    }

    override fun onBindData(data: AllBannerDataWrapper) {

        data.bannerList?.run {
            bannerList.addAll(this)
            bannerAdapter.notifyDataSetChanged()

            startSwitchJob()
        }

        data.emptyData?.run {
            bannerList.add(this)
            bannerAdapter.notifyDataSetChanged()
        }
    }


    override fun onClick(clickedView: View) {
        // do nothing
    }


    private fun startSwitchJob() {
        bannerSwitchJob?.run {
            if (!isActive) {
                bannerSwitchJob = getBannerSwitchJob().apply {
                    start()
                }
            }
        } ?: let {
            // 新建 job
            bannerSwitchJob = getBannerSwitchJob().apply {
                start()
            }
        }
    }

    private fun cancelJob() {
        bannerSwitchJob?.run {
            if (isActive) {
                cancel()
            }
        }
    }

    private fun getBannerSwitchJob(): Job {
        return GlobalScope.launch {
            repeat(Int.MAX_VALUE) {
                data.bannerList?.run {
                    if (size == 0) {
                        return@launch
                    }

                    // 2 秒
                    delay(2000)

                    currentIndex++
                    val index = currentIndex % size

                    recyclerView.smoothScrollToPosition(index)
                    currentIndex = index
                }
            }
        }
    }


    override fun onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow()
        cancelJob()
    }
}