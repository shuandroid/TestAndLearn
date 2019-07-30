package com.chendroid.learning.model

import com.chendroid.learning.ui.presenter.FirstHomePresenter

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
interface HomeModel {

    fun getBanner(onBannerListener: FirstHomePresenter.OnBannerListener)

    fun cancelBannerRequest()

    fun getHomeList(onHomeListListener: FirstHomePresenter.OnHomeListListener, page: Int = 0)

    fun cancelHomeListRequest()


}