package com.chendroid.learning.utils

import android.text.style.ClickableSpan
import android.util.Log
import android.view.View

/**
 * @intro 可点击的样式 span
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/29
 */
class TLClickableSpan : ClickableSpan() {

    override fun onClick(widget: View) {
        Log.i("zc_test", "TLClickableSpan is onClick and view is $widget")
    }
}