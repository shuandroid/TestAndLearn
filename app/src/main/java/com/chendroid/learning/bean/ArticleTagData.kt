package com.chendroid.learning.bean

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @intro 文章的 tag 数据
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-19
 */
@Parcelize
data class ArticleTagData(
        //
        var courseId: Int,
        // 该 tag 唯一 id， 后续请求数据使用
        var id: Int,
        // 名称
        var name: String?,
        // 订阅数
        var order: Long,
        // 父亲 tag id
        var parentChapterId: Int,
        // 是否可见
        var visible: Int,
        // 它包含的子 tag
        var children: List<ChildrenTagData>?

) : Parcelable