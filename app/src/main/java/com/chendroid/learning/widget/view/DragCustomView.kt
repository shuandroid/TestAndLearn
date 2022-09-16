package com.chendroid.learning.widget.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.chendroid.learning.R
import com.facebook.drawee.view.SimpleDraweeView

/**
 * @intro 自定义的可拖拽的 view， 利用 ViewDragHelper 实现 todo 会拦截全部界面的 click 事件
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/15
 */
class DragCustomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var viewDragHelper: ViewDragHelper

    // 头像 view
    private lateinit var avatarView: SimpleDraweeView

    // 原来的位置
    private var originPositionX = 0

    private var originPositionY = 0

    init {

        // 初始化 view
        val innerView = LayoutInflater.from(context).inflate(R.layout.custom_drag_view_layout, this, true)
        avatarView = innerView.findViewById(R.id.avatar_icon_view)

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && inAvatarViewInterval(event)) {
            Log.i("zc_test", "DragCustomView onInterceptTouchEvent event 拦截到")
            // 如果动画运行还没结束，这里需要结束，关闭掉
            return true
        }
        // 拦截交给 viewDragHelper
        return viewDragHelper.shouldInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        originPositionX = avatarView.left
        originPositionY = avatarView.top

        Log.i("zc_test", "DragCustomView onLayout originPositionX is $originPositionX and top is $originPositionY")

        initViewDragHelper()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.i("zc_test", "onFinishInflate")
        // 不可点击
        isClickable = false
        // 允许内部 view 显示在外部
        clipChildren = false

    }

    private fun initViewDragHelper() {

        // 回调
        viewDragHelper = ViewDragHelper.create(this.parent as ViewGroup, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                Log.i("zc_test", "DragCustomView tryCaptureView child is $child")
                if (child === avatarView) {
                    return true
                }
                return false
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return 1
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return 1
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                super.onViewPositionChanged(changedView, left, top, dx, dy)
                Log.i("zc_test", "DragCustomView onViewPositionChanged left is $left, top is $top,  and dx is $dx, dy is $dy")
            }

            // 当不再拖拽 view 时，会调用这里
            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)
                Log.i("zc_test", "DragCustomView onViewReleased $xvel and y is $yvel")
                handleViewReleased()
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return top
            }

        })

        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
    }


    /**
     * 处理被释放时的逻辑
     */
    private fun handleViewReleased() {
        setViewToOriginPosition()
    }

    /**
     * 设置 avatarView 回到原位置
     */
    private fun setViewToOriginPosition() {
        val offsetX = originPositionX - avatarView.left
        val offsetY = originPositionY - avatarView.top
        Log.i("zc_test", "动画开始时， 位置为 avatarView left  ${avatarView.left}, and top is ${avatarView.top}")
        Log.i("zc_test", "动画开始时， 位置为 avatarView x  ${avatarView.x}, and y is ${avatarView.y}")

        testSpring()
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

        val x = location[0]
        val y = location[1]

        val radius = (avatarView.right - avatarView.left) / 2

        val centerX = x + radius
        val centerY = y + radius

        // event 和 中心点坐标的差值
        val offsetX = Math.abs(eventX - centerX)
        val offsetY = Math.abs(eventY - centerY)

        val offsetRadius = Math.sqrt(Math.pow(offsetX.toDouble(), 2.0) + Math.pow(offsetY.toDouble(), 2.0))

        if (offsetRadius > radius) {
            return false
        }

        return true
    }


    private fun testSpring() {

        invalidate()
        requestLayout()

        // todo spring 动画

        // SpringForce 不能使用同一个，不然，finalPosition 会被覆盖掉
        val springX = SpringAnimation(avatarView, DynamicAnimation.X).setSpring(SpringForce().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY).setStiffness(SpringForce.STIFFNESS_MEDIUM))
        val springY = SpringAnimation(avatarView, DynamicAnimation.Y).setSpring(SpringForce().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY).setStiffness(SpringForce.STIFFNESS_MEDIUM))
        springX.addUpdateListener { animation, value, velocity ->
            Log.i("zc_test", "springX.addUpdateListener value is $value")
            avatarView.offsetLeftAndRight((value - avatarView.left).toInt())
        }

        springY.addUpdateListener { animation, value, velocity ->
            Log.i("zc_test", "springYYYYYYYYY.addUpdateListener value is $value")
            // todo  这里至关重要～因为 动画没有改变 left 和 top, 不改变的话,下次移动不了了
            avatarView.offsetTopAndBottom((value - avatarView.top).toInt())
        }
        Log.i("zc_test", "spring test animate originPositionX is $originPositionX, and originPositionY is  $originPositionY")
        springX.animateToFinalPosition(originPositionX.toFloat())
        springY.animateToFinalPosition(originPositionY.toFloat())
    }

}