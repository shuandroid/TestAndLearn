package com.chendroid.learning.widget.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.chendroid.learning.R
import com.chendroid.learning.utils.ViewOutlineProviderUtils
import dp
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @intro 随手指拖动而动的 View 控件
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/8
 */
class DragView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {


    // 是否在滑动的标识位
    private var mScrolling = false

    // 对多个手指做处理的标识位
    private var mIsCanMove = false
    private var isFirstMove = true
    private val isHasFold = false

    //该值是指是否支持拖拽，默认为 true， 依赖实验的配置
    private val isSupportDrag = true

    //  view 位置相关变量文章
    private var mPrevX = 0f
    private var mPrevY: kotlin.Float = 0f
    private var mCurX: kotlin.Float = 0f
    private var mCurY: kotlin.Float = 0f

    // 第一次手势 down, 按下时的 x, y 位置
    private var mTouchDownX = 0f
    private var mTouchDownY = 0f
    private var mFirstX = 0f
    private var mFirstY = 0f

    private var parentViewMaxHeight = 0F

    private var screenWidth: Float

    init {

        screenWidth = resources.displayMetrics.widthPixels.toFloat()

        // 初始化 view
        val innerView = LayoutInflater.from(context).inflate(R.layout.view_drag_add_layout, this, true)

        // 设置圆角背景
        ViewOutlineProviderUtils.setRoundCorner(this, 32.dp)
        apply {
            elevation = 4.dp.toFloat()
            isClickable = true
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // do nothing

        parentViewMaxHeight = (parent as ViewGroup).height.toFloat()
    }

    /**
     * 根据移动的距离，判断是否需要拦截该事件；如果移动距离 delta 符合移动条件，则返回 true, 拦截； 否则返回 false, 不拦截
     * 重写该方法的原因: 防止外部 view ,拦截了事件，传递不到该 view 中。
     */
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {

        //如果不支持拖拽，则不拦截
        if (!isSupportDrag) {
            return super.onInterceptTouchEvent(event)
        }

        //如果不支持拖拽，则不拦截
        // delta 为移动的距离
        val delta: Int
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchDownX = event.x
                mTouchDownY = event.y
                mScrolling = false
            }
            MotionEvent.ACTION_MOVE -> {
                // 判断是否符合移动的条件
                delta = sqrt((mTouchDownX - event.x.toDouble()).pow(2.0) + (mTouchDownY - event.y.toDouble()).pow(2.0)).toInt()
                mScrolling = delta >= ViewConfiguration.get(context).scaledTouchSlop

                Log.i("zc_test", "onInterceptTouchEvent ACTION_MOVE mScrolling is $mScrolling")
            }
            MotionEvent.ACTION_UP -> mScrolling = false
        }

        return mScrolling
    }

    /**
     * 处理事件
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (!isSupportDrag) {
            return super.onTouchEvent(event)
        }

        val delta: Int
        when (event!!.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> run {
                mTouchDownX = event.x
                mTouchDownY = event.y
                mScrolling = false
                mIsCanMove = isFirstFingerTouch(event)
                Log.i("zc_test", "onTouchEvent() mIsCanMove is $mIsCanMove")
            }

            MotionEvent.ACTION_MOVE -> run {

                Log.i("zc_test", "onTouchEvent() ACTION_MOVE ")
                if (!mIsCanMove) {
                    //
                    return@run
                }
                delta = sqrt((mTouchDownX - event.x.toDouble()).pow(2.0) + (mTouchDownY - event.y.toDouble()).pow(2.0)).toInt()
                if (delta < ViewConfiguration.get(context).scaledTouchSlop) {
                    //不满足对滑动距离的判断，则直接返回
                    return@run
                }
                Log.i("zc_test", "onTouchEvent() ACTION_MOVE 符合滑动 ")

                mScrolling = true

                if (isFirstMove) {
                    Log.i("zc_test", "onTouchEvent() isFirstMove is true ")
                    mFirstX = x
                    mFirstY = y
                    mPrevX = event.rawX
                    mPrevY = event.rawY
                    isFirstMove = false
                }
                val deltaX = event.rawX - mPrevX
                val deltaY = event.rawY - mPrevY
                mCurX = mFirstX + deltaX
                mCurY = mFirstY + deltaY

                handleViewWhenOverScreen()
                x = mCurX
                y = mCurY
            }

            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> if (isFirstFingerTouch(event)) {
                // 移动该 view
                moveViewToEdge()
                isFirstMove = true
                mIsCanMove = false
                if (mScrolling) {
                    mScrolling = false
                    return true
                }
            }
        }

        return super.onTouchEvent(event)
    }

    /**
     * 调整 View mCurY, mCurX， 避免当前 View 拖动超出可移动范围
     */
    private fun handleViewWhenOverScreen() {
        // Y 轴上边界
        if (mCurY <= 10.dp) {
            mCurY = 10.dp.toFloat()
        }

        // Y 轴下边界
        if (mCurY > parentViewMaxHeight - height) {
            mCurY = parentViewMaxHeight - height
        }

        // X 轴左边界
        if (mCurX <= 10.dp) {
            mCurX = 10.dp.toFloat()
        }

        // X 轴右边界
        if (mCurX > screenWidth - 10.dp - width) {
            mCurX = screenWidth - 10.dp - width
        }
    }

    /**
     * 只处理第一个手指的 touch event，防止多点触控引起的问题
     *
     * @return true if the
     */
    private fun isFirstFingerTouch(event: MotionEvent): Boolean {
        return event.getPointerId(event.actionIndex) == 0
    }

    /**
     * 移动 NextAnswerAnimationView 到可显示区域的边缘
     */
    private fun moveViewToEdge() {
        Log.i("zc_test", "moveViewToEdge()")

        val destX: Int = if (x + width / 2 > screenWidth / 2) {
            (screenWidth - width - 10.dp).toInt()
        } else {
            10.dp
        }
        animate().translationXBy(destX - x).setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (context == null) {
                            return
                        }
                    }
                })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

}