package com.chendroid.learning.ui.presenter

import android.support.annotation.NonNull
import android.util.Log
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import com.chendroid.learning.model.HomeModel
import com.chendroid.learning.model.HomeModelImpl
import com.chendroid.learning.ui.view.CollectArticleView
import com.chendroid.learning.ui.view.FirstHomeFragmentView

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
class FirstHomeFragmentPresenterImpl(
        private val homeFragmentView: FirstHomeFragmentView,
        private val collectArticleView: CollectArticleView

) : FirstHomePresenter.OnHomeListListener, FirstHomePresenter.OnBannerListener {


    private val homeModel: HomeModel = HomeModelImpl()

    override fun getBanner() {

        homeModel.getBanner(this)
    }

    override fun getBannerSuccess(result: HomeBanner) {
        Log.e("zc_test", "getBannerSuccess result is $result")
        if (result.errorCode != 0) {
            homeFragmentView.getBannerFailed(result.errorMsg)
            return
        }

        result.data ?: let {
            homeFragmentView.getBannerZero()
            return
        }

        homeFragmentView.getBannerSuccess(result)
    }

    override fun getBannerFailed(errorMessage: String?) {
        homeFragmentView.getBannerFailed(errorMessage)
    }

    // presenter 层去获取 model 数据， 通过接口实现 在 presenter 和 model 间传递数据
    override fun getHomeList(page: Int) {

        homeModel.getHomeList(this, page)
    }

    // 获取文章列表数据成功回调
    override fun getHomeListSuccess(@NonNull result: HomeListResponse) {
        // errorCode = 0 为正常请求
        if (result.errorCode != 0) {
            Log.i("zc_test", "获取的 errorCode 错误码不正确")

            homeFragmentView.getHomeListFailed(result.errorCode.toString())
            return
        }

        val total = result.data.total

        if (total == 0) {
            Log.i("zc_test", "获取的文章数量为 0， 返回")
            homeFragmentView.getHomeListZero()
            return
        }

        // 当第一页小于一页总数时
        if (total < result.data.size) {
            Log.i("zc_test", "获取的文章数量为小于 20 时, result is $result")
            homeFragmentView.getHomeListSmall(result)
            return
        }
        Log.i("zc_test", "获取的文章数量为 result is $result")

        homeFragmentView.getHomeListSuccess(result)
    }

    override fun getHomeListFailed(errorMessage: String?) {
        homeFragmentView.getHomeListFailed(errorMessage)
    }

    // 暂停网络请求
    fun cancelRequest() {
        homeModel.cancelBannerRequest()
        homeModel.cancelHomeListRequest()
    }


}