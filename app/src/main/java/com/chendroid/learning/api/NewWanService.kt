package com.chendroid.learning.api

import androidx.annotation.IntRange
import com.chendroid.learning.bean.*
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

    /**
     * 后去 Todo 列表
     * pageNum 页码，从 「1」开始
     * queryMap, 可添加 query 信息，
     * 「status」, 状态， 1 表示完成；0 未完成; 默认全部展示；
     * 「type」大于0的整数, 创建时传入的类型, 默认全部展示; 目前有「工作」、「学习」、「生活」
     * 「priority」大于0的整数, 创建时传入的优先级；默认全部展示
     * 「orderby」 1:完成日期顺序；2.完成日期逆序；3.创建日期顺序；4.创建日期逆序(默认)；
     * 例如：「/lg/todo/v2/list/{1}/json?status=0&orderby=1&type=」
     */
    @GET("/lg/todo/v2/list/{page_num}/json")
    suspend fun getTodoList(@Path("page_num") @IntRange(from = 1) pageNum: Int, @QueryMap queryMap: Map<String, Int>? = null): Response<TodoData>


    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addNewTodo(@Field("title") title: String, @Field("content") content: String,
                           @Field("type") type: Int): Response<CollectArticleResponse>

}