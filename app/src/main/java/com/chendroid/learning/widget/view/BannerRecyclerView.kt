package com.chendroid.learning.widget.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/22
 */
class BannerRecyclerView : RecyclerView {

    constructor(context: Context) : this(context, null)


    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 解决滑动冲突
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }

}