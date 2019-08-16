package com.chendroid.learning.model

import android.util.Log
import cancelByActive
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.api.Net
import com.chendroid.learning.api.WanAndroidService
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import com.chendroid.learning.ui.presenter.FirstHomePresenter
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import tryCatch

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
class HomeModelImpl : HomeModel {

    // 异步请求 banner
    private var bannerAsync: Deferred<HomeBanner>? = null

    // 异步请求文章列表
    private var homeListAsync: Deferred<HomeListResponse>? = null

    override fun getBanner(onBannerListener: FirstHomePresenter.OnBannerListener) {

        async(UI) {
            tryCatch({
                it.printStackTrace()
                onBannerListener.getBannerFailed(it.toString())
            }) {
                bannerAsync?.cancelByActive()
                bannerAsync = ApiServiceHelper.wanAndroidService.getBanner()

                val result = bannerAsync?.await()
                result ?: let {
                    //获取失败
                    onBannerListener.getBannerFailed("获取 banner 的" + Constant.RESULT_NULL)
                    return@async
                }
                //获取成功
                onBannerListener.getBannerSuccess(result!!)
            }
        }
    }

    override fun cancelBannerRequest() {
        bannerAsync?.cancelByActive()
    }

    // 获取文章列表
    override fun getHomeList(onHomeListListener: FirstHomePresenter.OnHomeListListener, page: Int) {

        async(UI) {
            tryCatch({
                it.printStackTrace()
                Log.i("zc_test", "请求文章列表失败")
                Log.i("zc_test", "请求文章列表失败" + it.printStackTrace())

                onHomeListListener.getHomeListFailed(it.toString())
            }) {
                homeListAsync?.cancelByActive()
                homeListAsync = ApiServiceHelper.wanAndroidService.getHomeList(page)

                val result = homeListAsync?.await()
                result ?: let {
                    Log.i("zc_test", "请求文章列表失败 2333")
                    onHomeListListener.getHomeListFailed(Constant.RESULT_NULL)
                    return@async
                }

                Log.i("zc_test", "请求文章列表成功 haHa")

                onHomeListListener.getHomeListSuccess(result!!)
            }
        }
    }

    override fun cancelHomeListRequest() {
        homeListAsync?.cancelByActive()
    }
}