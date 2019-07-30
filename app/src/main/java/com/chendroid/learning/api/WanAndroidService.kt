package com.chendroid.learning.api

import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/1/18
 */
interface WanAndroidService {


    /**
     * 首页的 banner 的请求
     */
    @GET("/banner/json")
    fun getBanner(): Deferred<HomeBanner>

    @GET("/article/list/{page}/json")
    fun getHomeList(@Path("page") page: Int) : Deferred<HomeListResponse>

}