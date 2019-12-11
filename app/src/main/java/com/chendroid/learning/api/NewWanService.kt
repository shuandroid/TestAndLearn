package com.chendroid.learning.api

import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @intron 新的 玩 Android 接口, 使用了 suspend 关键字
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-02
 */
interface NewWanService {

    /**
     * 首页的 banner 的请求
     */
    @GET("/banner/json")
    suspend fun getBanner(): Response<HomeBanner>

    /**
     * 获取文章列表
     */
    @GET("/article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): Response<HomeListResponse>

}