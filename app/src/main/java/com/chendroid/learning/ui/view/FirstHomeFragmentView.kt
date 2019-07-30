package com.chendroid.learning.ui.view

import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse

/**
 * @intro 这是 presenter 和 view 层数据传递的接口
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
interface FirstHomeFragmentView {

    fun getHomeListSuccess(result: HomeListResponse)

    fun getHomeListFailed(errorMessage: String?)

    fun getHomeListZero()

    /**
     * get Home list data less than 20
     */
    fun getHomeListSmall(result: HomeListResponse)

    fun getBannerSuccess(result: HomeBanner)

    fun getBannerFailed(errorMessage: String?)

    fun getBannerZero()

}