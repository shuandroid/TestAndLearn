package com.chendroid.learning.data.repo

import com.chendroid.care.data.Result
import com.chendroid.learning.bean.CollectArticleResponse
import com.chendroid.learning.data.source.CollectArticleDataSource

/**
 * @intro 收藏文章的 repository
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/4/15
 */
class CollectArticleRepo(private val collectArticleDataSource: CollectArticleDataSource) {

    suspend fun collectArticle(articleId: Int): Result<CollectArticleResponse> {
        return collectArticleDataSource.collectArticle(articleId)
    }

    suspend fun unCollectArticle(articleId: Int, originId: Int): Result<CollectArticleResponse> {
        return collectArticleDataSource.unCollectArticle(articleId, originId)
    }

}