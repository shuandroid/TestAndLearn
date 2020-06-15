package com.chendroid.learning.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import androidx.customview.widget.ViewDragHelper.*

/**
 * @intro 自定义的可拖拽的 view， 利用 ViewDragHelper 实现
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/15
 */
class DragCustomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr), Callback() {


    private lateinit var viewDragHelper: ViewDragHelper


    init {

        viewDragHelper = create(this, 1.0f, this)

    }

    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
        return true
    }

    override fun onViewDragStateChanged(state: Int) {
        super.onViewDragStateChanged(state)
    }

    override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
        super.onViewPositionChanged(changedView, left, top, dx, dy)
    }

}