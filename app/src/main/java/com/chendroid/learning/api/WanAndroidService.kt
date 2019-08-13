package com.chendroid.learning.api

import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import com.chendroid.learning.bean.LoginResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*

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
    fun getHomeList(@Path("page") page: Int): Deferred<HomeListResponse>


    @POST("user/login")
    @FormUrlEncoded
    fun loginWanAndroid(@Field("username") userName: String,
                        @Field("password") password: String): Deferred<LoginResponse>


}