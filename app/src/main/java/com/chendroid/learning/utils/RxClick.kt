package com.chendroid.learning.utils

import android.view.View
import com.jakewharton.rxbinding4.view.ViewAttachEvent
import com.jakewharton.rxbinding4.view.clicks
import com.trello.rxlifecycle4.RxLifecycle
import com.trello.rxlifecycle4.android.RxLifecycleAndroid
import java.util.concurrent.TimeUnit

/**
 * @intro 防止重复点击
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/30
 */
object RxClick {

    // 在 ms  内的重复点击是无效的
    fun onClick(view: View, listener: View.OnClickListener, ms: Long) {
        view.clicks()
                .throttleFirst(ms, TimeUnit.MILLISECONDS)
                .compose(RxLifecycleAndroid.bindView(view))
                .subscribe {
                    listener.onClick(view)
                }
    }

}