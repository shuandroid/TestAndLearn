package com.chendroid.care.data

import java.lang.Exception

/**
 * @intro 网络接口返回的结果
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-10-30
 */
sealed class Result<out T : Any> {
    // sealed 密封，使它的子类，只能在当前内部才能声明， 便于使用 when.
    /**
     * 请求成功的数据类
     */
    data class Success<out T : Any>(val data: T) : Result<T>()

    /**
     * 请求失败的数据类
     */
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success 请求成功 data=$data"
            is Error -> "Error 请求失败 exception=$exception"
        }
    }


}