package com.chendroid.learning.repository

import com.chendroid.care.data.Result
import com.chendroid.care.util.safeApiCall
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.bean.ArticleTagData
import com.chendroid.learning.bean.TagListResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException

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

        GlobalScope.async {

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
    fun getArticleList() = GlobalScope.launch {


    }


    suspend fun getArticleDataByKt() = safeApiCall(
            call = { requestArticleTag() },
            errorMessage = "请求出错啦～～～"
    )

    private suspend fun requestArticleTag(): Result<TagListResponse> {

        val responseTemp = ApiServiceHelper.wanAndroidService.getArticleTypeListTestAsync()

        val response = responseTemp.await()

        if (response.data != null) {

            return Result.Success(response)
        }

        return Result.Error(IOException("error message error code is ${response.errorCode} and body is ${response.errorMsg}"))
    }

}