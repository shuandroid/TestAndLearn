package com.chendroid.care.data

import java.lang.Exception

/**
 * @intro 网络接口返回的结果
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-10-30
 */
//密封，保证在包内被调用
sealed class Result<out T : Any> {

    /**
     * 请求成功的数据类
     */
    data class Success<out T : Any>(val data: T) : Result<T>()

    /**
     * 请求失败的数据类
     */
    data class Error(val exception: Exception) : Result<Nothing>()


    override fun toString(): String {
        return when(this) {
            // todo 为什么加 <*>
            is Success<*> -> "Success 请求成功 data=$data"
            is Error -> "Error 请求失败 exception=$exception"
        }
    }


}