package com.chendroid.learning.api

import com.chendroid.learning.bean.CollectArticleResponse
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.bean.HomeListResponse
import com.chendroid.learning.bean.LoginResponse
import retrofit2.Response
import retrofit2.http.*

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


    /**
     * 登陆
     */
    @POST("/user/login")
    @FormUrlEncoded
    suspend fun loginAccount(@Field("username") username: String,
                             @Field("password") password: String): Response<LoginResponse>

    /**
     * 收藏或者取消收藏文章
     */
    @POST("/lg/collect/{article_id}/json")
    suspend fun collectArticle(@Path("article_id") articleId: String): Response<CollectArticleResponse>

    // todo FormUrlEncoded 的作用
    /**
     * 取消收藏文章
     */
    @POST("/lg/uncollect/{article_id}/json")
    @FormUrlEncoded
    suspend fun unCollectArticle(@Path("article_id") articleId: String, @Field("originId") originId: Int = -1): Response<CollectArticleResponse>

}