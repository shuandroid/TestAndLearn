package com.chendroid.learning.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @intro 标签体系请求的数据
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-19
 */
data class TagListResponse(
        var errorCode: Int,
        var errorMsg: String,
        var data: List<ArticleTagData>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.createTypedArrayList(ArticleTagData)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(errorCode)
        parcel.writeString(errorMsg)
        parcel.writeTypedList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TagListResponse> {
        override fun createFromParcel(parcel: Parcel): TagListResponse {
            return TagListResponse(parcel)
        }

        override fun newArray(size: Int): Array<TagListResponse?> {
            return arrayOfNulls(size)
        }
    }
}