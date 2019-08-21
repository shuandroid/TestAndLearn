package com.chendroid.learning.repository

import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.bean.ArticleTagData
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * @intro MoreArticleTagFragment 的数据请求处理类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
class ArticleTagDataSource {

    interface ArticleTagListCallback {
        /**
         * 获取数据成功
         */
        fun success(articleTagList: List<ArticleTagData>)

        fun error(errorMsg: String)
    }

    /**
     * 获取更多文章类型列表
     */
    @SuppressWarnings("checkResult")
    fun getMoreArticleTreeList(callback: ArticleTagListCallback) {

        async(UI) {

            val articleTypeList = ApiServiceHelper.wanAndroidService.getArticleTypeList()
            val result = articleTypeList.await()

            if (result.errorCode != 0) {
                callback.error("错误信息为" + result.errorMsg)
                return@async
            }

            result.data ?: let {
                // 如果数据为空， 没有文章类型
                callback.error("没有拿到文章类型数据")
                return@async
            }

            // 成功
            result.data?.run {
                callback.success(this)
            }
        }
    }

    /**
     * 获取类型下的文章列表
     */
    fun getArticleList() {

    }


}