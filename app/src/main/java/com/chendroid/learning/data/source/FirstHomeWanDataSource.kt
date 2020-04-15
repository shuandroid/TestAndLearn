package com.chendroid.learning.data.source

import com.chendroid.care.data.Result
import com.chendroid.care.util.safeApiCall
import com.chendroid.learning.api.NewWanService
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import java.lang.Exception

/**
 * @intro 首页获取数据的源头, 并且处理数据
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-02
 */
class FirstHomeWanDataSource(private val wanService: NewWanService) {


    suspend fun getBanner(): Result<HomeBanner> {

        return safeApiCall(call = { getBannerReally() },
                errorMessage = "获取 banner 失败"
        )
    }

    /**
     * 真正获取 banner 的位置
     * 加工数据
     */
    private suspend fun getBannerReally(): Result<HomeBanner> {

        val bannerResult = wanService.getBanner()
        // 成功时
        if (bannerResult.isSuccessful) {
            val body = bannerResult.body()
            if (body != null) {
                return Result.Success(body)
            }
        }

        return Result.Error(Exception("获取 banner 失败 error code ${bannerResult.code()} error body is ${bannerResult.errorBody()} "))
    }


    suspend fun getArticleList(page: Int): Result<HomeListResponse> {

        return safeApiCall(
                call = {
                    getReallyArticleList(page)
                },
                errorMessage = "获取文章失败"
        )
    }

    private suspend fun getReallyArticleList(page: Int): Result<HomeListResponse> {

        val articleResult = wanService.getArticleList(page)

        if (articleResult.isSuccessful) {
            val body = articleResult.body()
            body?.run {
                return Result.Success(this)
            }
        }

        return Result.Error(Exception("获取 banner 失败 error code ${articleResult.code()} error body is ${articleResult.errorBody()} "))
    }

}