package com.chendroid.learning.utils

import android.graphics.Outline
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import dp


/**
 * view 的扩展方法汇总
 */

/**
 * 判断当前 view 是否可见
 */
fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

/**
 * 为当前 view 设置四周圆角,
 * radiusPx 单位为 dp
 */
fun View.setRoundCorner(radiusDp: Int) {
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radiusDp.dp.toFloat())
        }
    }
    // 刷新
    invalidateOutline()
}

fun View.marginTop(marginTopDp: Int) {
    val params = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.topMargin = marginTopDp.dp
    }
    layoutParams = params
}

fun View.marginBottom(marginBottomDp: Int) {
    val params = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.bottomMargin = marginBottomDp.dp
    }
    layoutParams = params
}

fun View.marginStart(marginStartDp: Int) {
    val params = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.marginStart = marginStartDp.dp
    }
    layoutParams = params
}

fun View.marginEnd(marginEndDp: Int) {
    val params = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.marginEnd = marginEndDp.dp
    }
    layoutParams = params
}





