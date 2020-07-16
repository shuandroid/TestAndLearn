package com.chendroid.learning.repository

import android.util.Log
import com.chendroid.care.data.Result
import com.chendroid.care.data.Result.Success
import com.chendroid.care.util.safeApiCall
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.bean.ArticleTagData
import java.io.IOException

/**
 * @intro MoreArticleTagFragment 的数据请求处理类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
class ArticleTagDataSource {

    suspend fun getArticleTypeList() = safeApiCall(
            call = { requestArticleTag() },
            errorMessage = "请求出错啦～～～"
    )

    private suspend fun requestArticleTag(): Result<List<ArticleTagData>> {
        Log.i("zc_test", "requestArticleTag")
        val response = ApiServiceHelper.newWanService.getArticleTypeList()
        Log.i("zc_test", "requestArticleTag() response is $response")
        if (response.isSuccessful) {
            val body = response.body()
            body?.run {
                return Success(data!!)
            }
        }

        Log.i("zc_test", "error message error code is ${response.body()!!.errorCode} and body is ${response.body()!!.errorMsg}")
        return Result.Error(IOException("error message error code is ${response.body()!!.errorCode} and body is ${response.body()!!.errorMsg}"))
    }

}