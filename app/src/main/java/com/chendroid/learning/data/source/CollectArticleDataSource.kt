package com.chendroid.learning.data.source

import com.chendroid.care.data.Result
import com.chendroid.care.util.safeApiCall
import com.chendroid.learning.api.NewWanService
import com.chendroid.learning.bean.CollectArticleResponse
import java.lang.Exception

/**
 * @intro 收藏文章数据源
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/4/15
 */
class CollectArticleDataSource(private val wanService: NewWanService) {

    /**
     * 收藏文章
     */
    suspend fun collectArticle(articleId: Int): Result<CollectArticleResponse> {
        return safeApiCall(
                call = {
                    collectReallyArticle(articleId)
                },
                errorMessage = "收藏失败"
        )
    }

    private suspend fun collectReallyArticle(articleId: Int): Result<CollectArticleResponse> {

        val response = wanService.collectArticle(articleId.toString())

        if (response.isSuccessful) {
            val body = response.body()
            body?.run {
                return Result.Success(this)
            }
        }

        return Result.Error(Exception("收藏文章 失败 error code ${response.code()} error body is ${response.errorBody()} "))
    }

    /**
     * 取消收藏
     */
    suspend fun unCollectArticle(articleId: Int, originId: Int): Result<CollectArticleResponse> {
        return safeApiCall(
                call = {
                    unCollectReallyArticle(articleId, originId)
                },
                errorMessage = "收藏失败"
        )
    }

    private suspend fun unCollectReallyArticle(articleId: Int, originId: Int): Result<CollectArticleResponse> {
        var originIdReal = -1
        if (originId > 0) {
            originIdReal = originId
        }
        val response = wanService.unCollectArticle(articleId.toString(), originIdReal)
        if (response.isSuccessful) {
            val body = response.body()
            body?.run {
                return Result.Success(this)
            }
        }

        return Result.Error(Exception("收藏文章 失败 error code ${response.code()} error body is ${response.errorBody()} "))
    }
}