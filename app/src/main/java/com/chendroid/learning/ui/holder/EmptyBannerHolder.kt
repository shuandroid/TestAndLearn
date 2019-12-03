package com.chendroid.learning.ui.holder

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.chendroid.learning.R
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import dp

/**
 * @intro banner 为空时的空 holder
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-03
 */
@Layout(R.layout.holder_empty_banner_layout)
class EmptyBannerHolder(view: View) : SugarHolder<EmptyBannerData>(view) {

    override fun onBindData(data: EmptyBannerData) {
        // 设置 itemView 的圆角
        itemView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 5.dp.toFloat())
            }
        }
        itemView.clipToOutline = true
    }
}

data class EmptyBannerData(val type: String)

