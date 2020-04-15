package com.chendroid.learning.data.repo

import com.chendroid.care.data.Result
import com.chendroid.learning.bean.BaseData
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.data.source.FirstHomeWanDataSource

/**
 * @intro 首页 wan 的 repo 数据处理类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-02Ø
 */
class FirstHomeWanRepo(private val firstHomeWanDataSource: FirstHomeWanDataSource) {

    /**
     * 获取数据
     */
    suspend fun getBanner(): Result<List<HomeBanner.BannerItemData>> {
        val result = firstHomeWanDataSource.getBanner()

        if (result is Result.Success) {
            result.data.data?.run {

                return Result.Success(this)
            }
        }

        // 失败信息
        return Result.Error(Exception(result.toString()))
    }

    suspend fun getArticle(page: Int = 0): Result<BaseData> {
        val result = firstHomeWanDataSource.getArticleList(page)

        if (result is Result.Success) {
            result.data.data.run {
                return Result.Success(this)
            }
        }

        return Result.Error(Exception(result.toString()))

    }
}