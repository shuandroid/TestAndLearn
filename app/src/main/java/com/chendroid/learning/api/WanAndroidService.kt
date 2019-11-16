package com.chendroid.learning.api

import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import com.chendroid.learning.bean.LoginResponse
import com.chendroid.learning.bean.TagListResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
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

    /**
     * 获取更多文章类型列表
     */
    @GET("/tree/json")
    fun getArticleTypeList(): Deferred<TagListResponse>

    /**
     * 获取更多文章类型列表
     */
    @GET("/tree/json")
    fun getArticleTypeListTestAsync(): Deferred<TagListResponse>

    /**
     * 获取类型下的文章列表
     * query cid 为该文章的类型标志
     */
    @GET("/article/list/{page}/json")
    fun getArticleList(@Path("page") page: Int,
                       @Query("cid") cid: Int)


}