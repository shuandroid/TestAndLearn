package com.chendroid.learning.ui.presenter

import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
interface FirstHomePresenter {

    interface OnHomeListListener {

        fun getHomeList(page: Int = 0)

        fun getHomeListSuccess(result: HomeListResponse)

        fun getHomeListFailed(errorMessage: String?)
    }

    /**
     * get banner listener
     */
    interface OnBannerListener {

        fun getBanner()
        /**
         * get banner success
         */
        fun getBannerSuccess(result: HomeBanner)

        fun getBannerFailed(errorMessage: String?)
    }
}