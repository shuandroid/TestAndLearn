package com.chendroid.learning.bean

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
data class HomeListResponse(
        var errorCode: Int,
        var errorMsg: String?,
        val data: BaseData
)