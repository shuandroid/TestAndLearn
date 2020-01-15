package com.chendroid.learning.widget.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.chendroid.learning.R
import com.zhihu.android.sugaradapter.SugarAdapter
import dp
import kotlin.math.roundToInt

/**
 * @intro 自定义分割线
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
class CustomItemDecoration private constructor(private val context: Context) : RecyclerView.ItemDecoration() {


    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mDividerHeight: Int = 5.dp
    private val mDividerColorRes: Int
    private val padding: Int = 0

    init {
        mPaint.style = Paint.Style.FILL
        mDividerColorRes = R.color.f6f6f6
        mDividerHeight = 5.dp
    }

    fun setDividerHeight(@IntRange(from = 1) height: Int): CustomItemDecoration {
        mDividerHeight = height
        return this
    }

    // 改动，由 onDrawOver() 修改为 onDraw(). 因为 onDrawOver 这个时机去绘制分割条会使得分割条在 view 层级的最上方，
    // 可能会出现遮挡 RecyclerView scrollbar 的情况
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        mPaint.color = ContextCompat.getColor(context, mDividerColorRes)

        val left = parent.paddingLeft + padding
        val right = parent.width - parent.paddingRight - padding

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            if (!isLastItem(child, parent) && !isNoDividerItem(child, parent)) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom - params.bottomMargin + child.translationY.roundToInt()
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), (top + mDividerHeight).toFloat(), mPaint)
            }
        }
    }

    companion object {
        fun with(context: Context): CustomItemDecoration {
            return CustomItemDecoration(context)
        }
    }


    /**
     * 是否是最后一个 item
     */
    private fun isLastItem(view: View, parent: RecyclerView): Boolean {
        val childPosition = parent.getChildAdapterPosition(view)
        if (childPosition + 1 == (parent.adapter as SugarAdapter).itemCount) {
            // 是最后一个 item
            return true
        }
        return false
    }

    private fun isNoDividerItem(view: View, parent: RecyclerView): Boolean {
        val childPosition = parent.getChildAdapterPosition(view)
        if (childPosition == 0) {
            return true
        }

        return false
    }


    /**
     * 必须重写该方法，不然分割线占据的是 item 的空间位置
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (!isLastItem(view, parent) && !isNoDividerItem(view, parent)) {
            outRect.set(0, 0, 0, mDividerHeight)
        }
    }

}