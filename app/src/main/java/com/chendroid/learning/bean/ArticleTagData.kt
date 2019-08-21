package com.chendroid.learning.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @intro 文章的 tag 数据
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-19
 */
data class ArticleTagData(
        //
        var courseId: Int,
        // 该 tag 唯一 id， 后续请求数据使用
        var id: Int,
        // 名称
        var name: String,
        // 订阅数
        var order: Long,
        // 父亲 tag id
        var parentChapterId: Int,
        // 是否可见
        var visible: Int,
        // 它包含的子 tag
        var children: List<ChildrenTagData>?

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createTypedArrayList(ChildrenTagData)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeLong(order)
        parcel.writeInt(parentChapterId)
        parcel.writeInt(visible)
        parcel.writeTypedList(children)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleTagData> {
        override fun createFromParcel(parcel: Parcel): ArticleTagData {
            return ArticleTagData(parcel)
        }

        override fun newArray(size: Int): Array<ArticleTagData?> {
            return arrayOfNulls(size)
        }
    }
}