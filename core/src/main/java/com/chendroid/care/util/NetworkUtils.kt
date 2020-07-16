package com.chendroid.care.util

import android.util.Log
import com.chendroid.care.data.Result

import java.io.IOException

/**
 * @intro 网络请求的工具类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-10-30
 */

/**
 *  suspend 挂起，处理网络请求
 */
suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> {

    return try {
        call()
    } catch (e: Exception) {
        Log.i("zc_test", "safeApiCall error, exception is $e")
        Result.Error(IOException(errorMessage, e))
    }
}