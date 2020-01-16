package com.chendroid.learning.data.usecase

import android.util.Log
import com.chendroid.care.data.Result
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.data.repo.FirstHomeWanRepo
import com.chendroid.learning.data.source.FirstHomeWanDataSource

/**
 * @intro 获取 wanandroid 的 banner 数据的封装 useCase
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-02
 */
class GetBannerUseCase {

    private val firstHomeRepo by lazy {
        FirstHomeWanRepo(FirstHomeWanDataSource(ApiServiceHelper.newWanService))
    }

    /**
     * 获取 banner 信息
     */
    suspend fun getWanAndroidBanner(): Result<List<HomeBanner.BannerItemData>> {
        val result = firstHomeRepo.getBanner()
        Log.i("zc_test", "hahahha current thread is ${Thread.currentThread()}")
        return result
    }
}