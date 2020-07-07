package com.chendroid.learning.bean

import com.google.gson.annotations.SerializedName

/**
 * @intro 文章基础数据
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
data class BaseData(
//        @SerializedName("")
        var offset: Int,
        var size: Int,
        var total: Int,
        var pageCount: Int,
        var curPage: Int,
        var over: Boolean,
        var datas: List<BaseDatas>?
)