package com.chendroid.learning.widget.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.chendroid.learning.R
import com.chendroid.learning.utils.ViewOutlineProviderUtils
import com.facebook.drawee.view.SimpleDraweeView
import dp
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @intro
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/17
 */
class CustomDragView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
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

    /**
     * 原始的位置
     */
    private var originPositionX = 0F
    private var originPositionY = 0F

    // 头像 view
    private lateinit var avatarView: SimpleDraweeView

    init {

        screenWidth = resources.displayMetrics.widthPixels.toFloat()

        // 初始化 view
        val innerView = LayoutInflater.from(context).inflate(R.layout.custom_drag_view_layout, this, true)
        avatarView = innerView.findViewById(R.id.avatar_icon_view)

        apply {
            isClickable = true
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // do nothing

        parentViewMaxHeight = (parent as ViewGroup).height.toFloat()

        // 获取原始 view 的位置
        originPositionX = x
        originPositionY = y
    }

    override fun onFinishInflate() {
        super.onFinishInflate()


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

                moveViewByDrag()
            }

            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> if (isFirstFingerTouch(event)) {
                // 移动该 view
                resetView()
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

    private fun moveViewByDrag() {
        // 移动 view x, y 的坐标
        x = mCurX
        y = mCurY

        // todo 设置其他部分实现

    }

    /**
     *  重置 view
     */
    private fun resetView() {

        // SpringForce 不能使用同一个，不然，finalPosition 会被覆盖掉
        val springX = SpringAnimation(this, DynamicAnimation.X).setSpring(SpringForce().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY).setStiffness(SpringForce.STIFFNESS_MEDIUM))
        val springY = SpringAnimation(this, DynamicAnimation.Y).setSpring(SpringForce().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY).setStiffness(SpringForce.STIFFNESS_MEDIUM))

        Log.i("zc_test", "spring test animate originPositionX is $originPositionX, and originPositionY is  $originPositionY")
        springX.animateToFinalPosition(originPositionX)
        springY.animateToFinalPosition(originPositionY)
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


    private fun fetchBitmap() {

        val bitmap = Bitmap.createBitmap(avatarView.width, avatarView.height, Bitmap.Config.ARGB_8888)

        val locationOfViewInWindow = IntArray(2)
        avatarView.getLocationInWindow(locationOfViewInWindow)





    }

}