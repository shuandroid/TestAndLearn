package com.chendroid.learning.bean

/**
 * @intro 收藏/取消收藏文章的数据返回类
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/4/14
 */
data class CollectArticleResponse(
        // errorCode 为负，则表示请求错误，这时 errorMsg 会有值。 errorCode = 0 代表执行成功
        var errorCode: Int,
        var errorMsg: String
)