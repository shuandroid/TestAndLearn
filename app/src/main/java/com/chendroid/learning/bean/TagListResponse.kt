package com.chendroid.learning.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @intro 标签体系请求的数据
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-19
 */
@Parcelize
data class TagListResponse(
        var errorCode: Int,
        var errorMsg: String,
        var data: List<ArticleTagData>?
) : Parcelable