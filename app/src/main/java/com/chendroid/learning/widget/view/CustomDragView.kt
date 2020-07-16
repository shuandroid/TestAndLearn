package com.chendroid.learning.widget.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.chendroid.learning.R
import com.chendroid.learning.utils.ViewOutlineProviderUtils
import com.facebook.drawee.view.SimpleDraweeView
import dp
import java.lang.Exception
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @intro 可拖拽 view，使用 onTouchEvent 处理
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

    lateinit var targetBitmap: Bitmap

    lateinit var bitmapPaint: Paint

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

        bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//        bitmapPaint.alpha = 1
        bitmapPaint.style = Paint.Style.FILL
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

//        if (!inAvatarViewInterval(event)) {
////            return super.onInterceptTouchEvent(event)
////        }

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
    override fun onTouchEvent(event: MotionEvent): Boolean {

////        // 如果不再头像区域，则不拦截
//        if (!inAvatarViewInterval(event)) {
//            return super.onTouchEvent(event)
//        }

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

                mScrolling = true

                if (isFirstMove) {
//                    Log.i("zc_test", "onTouchEvent() isFirstMove is true ")
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
        // 移动 view x, y 的坐标 设置为对应的 avatar view
        // 头像的位置
//        avatarView.x = mCurX
//        avatarView.y = mCurY
        x = mCurX
        y = mCurY
    }

    /**
     * 是否 event 位于 avatarView 里面
     */
    private fun inAvatarViewInterval(event: MotionEvent): Boolean {

        val eventX = event.rawX
        val eventY = event.rawY

        // 获取当前 view 在屏幕的位置
        val location = IntArray(2)
        avatarView.getLocationOnScreen(location)

        val avatarX = location[0]
        val avatarY = location[1]

        Log.i("zc_test", "inAvatarViewInterval x is $avatarX, and y is $avatarY ")

        val radius = (avatarView.right - avatarView.left) / 2

        val centerX = avatarX + radius
        val centerY = avatarY + radius

        // event 和 中心点坐标的差值
        val offsetX = Math.abs(eventX - centerX)
        val offsetY = Math.abs(eventY - centerY)

        val offsetRadius = Math.sqrt(Math.pow(offsetX.toDouble(), 2.0) + Math.pow(offsetY.toDouble(), 2.0))

        if (offsetRadius > radius) {
            Log.i("zc_test", "inAvatarViewInterval return false ")

            return false
        }

        Log.i("zc_test", "inAvatarViewInterval return true ")

        return true
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

    // todo 这种方式不行，不能为全屏的 view ,会遮挡被覆盖 view 的点击事件。另想其他的方式
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
//
        Log.i("zc_test", "dispatchDraw hahhahah")
//        if (mScrolling) {
//            canvas?.run {
//                Log.i("zc_test", "dispatchDraw hahhahah drawBitmap")
////                drawBitmap(targetBitmap, 0F, 0F, bitmapPaint)
//            }
//        }
    }

}