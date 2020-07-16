package com.chendroid.learning.utils

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel

/**
 * @intro 给 `View` 通过  ViewOutlineProvider 设置圆角的工具类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-06
 */
object ViewOutlineProviderUtils {

    /**
     * 为当前 view 设置四周圆角
     */
    @JvmStatic
    fun setRoundCorner(roundView: View, radiusPx: Int) {
        roundView.clipToOutline = true
        roundView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radiusPx.toFloat())
            }
        }
        // 刷新
        roundView.invalidateOutline()
    }

    /**
     * 设置该 view 的上面部分的两个角有圆角，下面部分为正常
     */
    @JvmStatic
    fun setTopPartRoundCorner(roundView: View, radius: Int) {
        roundView.clipToOutline = true
        roundView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height + radius, radius.toFloat())
            }
        }

        // 只刷新轮廓
        roundView.invalidateOutline()
    }

}