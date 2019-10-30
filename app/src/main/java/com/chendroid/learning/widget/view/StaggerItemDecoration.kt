package com.chendroid.learning.widget.view

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import com.chendroid.learning.R
import com.zhihu.android.sugaradapter.SugarAdapter
import dp

/**
 * @intro 自定义分割线, 适用于瀑布流的分割线
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
class StaggerItemDecoration private constructor(private val context: Context) : RecyclerView.ItemDecoration() {


    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mDividerHeight: Int = 8.dp
    private val mDividerColorRes: Int
    private val padding: Int = 0

    // 一行最多有两个 card
    private val maxSpan = 2

    init {
        mPaint.style = Paint.Style.FILL
        mDividerColorRes = R.color.f6f6f6
        mDividerHeight = 8.dp
    }

    fun setDividerHeight(@IntRange(from = 1) height: Int): StaggerItemDecoration {
        mDividerHeight = height
        return this
    }

    companion object {
        fun with(context: Context): StaggerItemDecoration {
            return StaggerItemDecoration(context)
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


    /**
     * 必须重写该方法，不然分割线占据的是 item 的空间位置
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        var spanIndex = 0
        if (view.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        }

        outRect.left = mDividerHeight / 2
        outRect.right = mDividerHeight / 2
        if (parent.getChildAdapterPosition(view) < maxSpan) {
            outRect.top = 0
        } else {
            outRect.top = mDividerHeight
        }

        if (spanIndex == 0) {
            outRect.left += mDividerHeight / 2
        } else if (spanIndex == maxSpan - 1) {
            outRect.right += mDividerHeight / 2
        }
    }

}