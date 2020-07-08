package com.chendroid.learning.widget.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.chendroid.learning.utils.ViewUtils
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @intro 测试测试
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/7/2
 */
class TestConstraintLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {


    // 第一次手势 down, 按下时的 x, y 位置
    private var mTouchDownX = 0f
    private var mTouchDownY = 0f
    private var mFirstX = 0f
    private var mFirstY = 0f

    //  view 位置相关变量文章
    private var mPrevX = 0f
    private var mPrevY = 0f
    private var mCurX = 0f
    private var mCurY = 0f

    private var mFirstEventX = 0f
    private var mFirstEventY = 0f

    // 是否在滑动的标识位
    private var mScrolling = false

    // 是否正在重设 view 位置
    private var isResettingView = false

    // 对多个手指做处理的标识位
    private var mIsCanMove = false

    // 是否为第一次移动
    private var isFirstMove = true

    private lateinit var targetDragView: View

    lateinit var targetBitmap: Bitmap

    var bitmapPaint: Paint

    // 第二个残影的 bitmap 画笔
    var secondAlphaPaint: Paint

    // 第一个 bitmap 的坐标
    var firstX = 0F
    var firstY = 0F
    var secondX = 0F
    var secondY = 0F

    var thirdX = 0F
    var thirdY = 0F

    // 缩放因子
    var moveValue = 0f


    // 手势移动速度追踪， 用于计算速度
    var velocityTracker: VelocityTracker? = null

    // 速度, 向左移动，则 xEventVelocity < 0, 向右移动，则 xEventVelocity > 0
    var xEventVelocity = 0f

    //速度, 向上移动，则 yEventVelocity < 0, 向下移动，则 yEventVelocity > 0
    var yEventVelocity = 0f

    var count = 1


    var firstAnimatorSet: AnimatorSet? = null
    var secondAnimatorSet: AnimatorSet? = null
    var thirdAnimator: ValueAnimator? = null

    init {

        apply {
            isClickable = true
        }

        bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        bitmapPaint.style = Paint.Style.FILL
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        secondAlphaPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        secondAlphaPaint.apply {
            style = Paint.Style.FILL
            alpha = (255 * (0.5)).toInt()
        }

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {

        // down 的时候判断
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (!ViewUtils.isTouchEventInTargetView(event, targetDragView)) {
                //
                Log.i("zc_test", "TestConstraintLayout  onInterceptTouchEvent 不拦截")

                val result = super.onInterceptTouchEvent(event)
                Log.i("zc_test", "TestConstraintLayout onInterceptTouchEvent result is $result ")
                return result
            }
        }

        Log.i("zc_test", "TestConstraintLayout  onInterceptTouchEvent 该拦截了")

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

                Log.i("zc_test", "TestConstraintLayout onInterceptTouchEvent ACTION_MOVE mScrolling is $mScrolling")
            }
            MotionEvent.ACTION_UP -> mScrolling = false
        }

        Log.i("zc_test", "TestConstraintLayout onInterceptTouchEvent scroll is $mScrolling")

        return mScrolling
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i("zc_test", "TestConstraintLayout onTouchEvent")
        // down 的时候判断
//        if (event.action == MotionEvent.ACTION_DOWN) {
//            if (!ViewUtils.isTouchEventInTargetView(event, targetDragView)) {
//                //
//                Log.i("zc_test", "TestConstraintLayout  onTouchEvent 不拦截")
//
//                val result = super.onTouchEvent(event)
//                Log.i("zc_test", "TestConstraintLayout onTouchEvent result is $result ")
//
//                return result
//            }
//        }

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }

        velocityTracker?.run {
            // 添加进当前事件
            addMovement(event)
        }

        val delta: Int
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> run {
                mTouchDownX = event.x
                mTouchDownY = event.y
                mScrolling = false
                mIsCanMove = isFirstFingerTouch(event)
                Log.i("zc_test", "onTouchEvent() mIsCanMove is $mIsCanMove")
            }

            MotionEvent.ACTION_MOVE -> run {

//                Log.i("zc_test", "onTouchEvent() ACTION_MOVE ")
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
                    mFirstX = targetDragView.x
                    mFirstY = targetDragView.y
                    mPrevX = event.rawX
                    mPrevY = event.rawY
                    mFirstEventX = event.rawX
                    mFirstEventY = event.rawY

                    firstX = mFirstX
                    firstY = mFirstY

                    secondX = mFirstX
                    secondY = mFirstY

                    thirdX = mFirstX
                    thirdY = mFirstY

                    isFirstMove = false
                }

                val deltaX = event.rawX - mFirstEventX
                val deltaY = event.rawY - mFirstEventY

                // 这里测试,
                // 仍然不和谐
                mPrevX = mCurX
                mPrevY = mCurY

                count++

                mCurX = mFirstX + deltaX
                mCurY = mFirstY + deltaY

                // 速率是否需要使用
                velocityTracker?.run {
                    computeCurrentVelocity(1000)
                    xEventVelocity = getXVelocity()
                    yEventVelocity = getYVelocity()
                }

                handleDragMoving()
            }

            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> if (isFirstFingerTouch(event)) {
                handleDragOver()
                isFirstMove = true
                mIsCanMove = false
                if (mScrolling) {
                    mScrolling = false
                    return true
                }
            }
        }

        val touchEventResult = super.onTouchEvent(event)
        Log.i("zc_test", "TestConstraintLayout onTouchEvent result is $touchEventResult ")
        return touchEventResult
    }

    fun setTargetView(view: View) {
        targetDragView = view
    }

    fun setTestTargetBitmap(bitmap: Bitmap) {

        // 圆形 bitmap ，与原 bitmap 一样大小
        val circleBitmap = Bitmap.createBitmap(targetDragView.width, targetDragView.height, Bitmap.Config.ARGB_8888)
        // 画布
        val testCanvas = Canvas(circleBitmap)

        // 设置画笔
        val testPaint = Paint()
        testPaint.isAntiAlias = true

        //
        testCanvas.drawCircle((targetDragView.width / 2).toFloat(), (targetDragView.height / 2).toFloat(), (targetDragView.width / 2).toFloat(), testPaint)
        testPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        testCanvas.drawBitmap(bitmap, 0F, 0F, testPaint)
        // 把原来的方形 bitmap 设置成了圆形的 bitmap
        targetBitmap = circleBitmap

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
     * 处理正在拖拽行为
     */
    private fun handleDragMoving() {

        targetDragView.visibility = View.INVISIBLE

        Log.i("zc_test", "handleDragMoving 这里 mCurX is $mCurX, mPrevX  is $mPrevX， mCurY is $mCurY，mPrevY is $mCurY  ")

        if (mCurX == mPrevX && mCurY == mPrevY) {
            // 悬停时，不刷新
            return
        }

        // 第一个 bitmap 设置动画
        startFirstBitmapAnimator()

        startSecondBitmapAnimator()

        startThirdBitmapAnimator()
    }

    private fun startFirstBitmapAnimator() {
        // 第一个 bitmap 的动画
        firstAnimatorSet?.run {
            if (isRunning) {
                return
            } else {
                removeAllListeners()
                cancel()
            }
        }

        val animatorStartX = firstX
        val animatorStartY = firstY

        val firstXAnimator = ValueAnimator.ofFloat(0F, 1F)
        val firstYAnimator = ValueAnimator.ofFloat(0F, 1F)

        firstXAnimator.addUpdateListener {
            if (firstX == mCurX && firstY == mCurY) {
                firstAnimatorSet?.apply {
                    removeAllListeners()
                    cancel()
                }
                firstAnimatorSet?.cancel()
                return@addUpdateListener
            }
            firstX = (it.animatedValue as Float) * (mCurX - animatorStartX) + animatorStartX
        }

        firstYAnimator.addUpdateListener {
            firstY = (it.animatedValue as Float) * (mCurY - animatorStartY) + animatorStartY
        }

        firstAnimatorSet = AnimatorSet()
        firstAnimatorSet?.apply {
            playTogether(firstXAnimator, firstYAnimator)
            interpolator = LinearInterpolator()
            duration = 25
        }

        firstAnimatorSet?.start()
    }

    // 第二个 bitmap 的绘制在这里改变
    private fun startSecondBitmapAnimator() {
        // 使用动画
        secondAnimatorSet?.run {
            // 如果正在运行，则不影响它的运行，下次再开启动画
            if (isRunning) {
                return
            }

            removeAllListeners()
            cancel()
        }

        val animatorStartX = secondX
        val animatorStartY = secondY
        // 这样不和谐
        val targetX = mCurX
        val targetY = mCurY
        secondAnimatorSet = AnimatorSet()

        val xSecondValueAnimator = ValueAnimator.ofFloat(0F, 1F)
        xSecondValueAnimator?.addUpdateListener {

            if (secondX == targetX && secondY == targetY) {
                secondAnimatorSet?.apply {
                    removeAllListeners()
                    cancel()
                }
                return@addUpdateListener
            }

            if (secondX == mCurX && secondY == mCurY) {
                secondAnimatorSet?.apply {
                    removeAllListeners()
                    cancel()
                }
                return@addUpdateListener
            }
            secondX = (it.animatedValue as Float) * (targetX - animatorStartX) + animatorStartX
            secondY = (it.animatedValue as Float) * (targetY - animatorStartY) + animatorStartY
//            invalidate()
        }

        secondAnimatorSet?.run {
            playTogether(xSecondValueAnimator)
            interpolator = AccelerateDecelerateInterpolator()
            duration = 75
        }

        secondAnimatorSet?.start()
    }

    // 开启第三个 bitmap 位置计算
    private fun startThirdBitmapAnimator() {

        // 使用动画
        thirdAnimator?.run {
            // 如果正在运行，则不影响它的运行，下次再开启动画
            if (isRunning) {
                return
            }
            removeAllListeners()
            cancel()
        }

        val targetX = mCurX
        val targetY = mCurY

        val animatorStartX = thirdX
        val animatorStartY = thirdY

        thirdAnimator = ValueAnimator.ofFloat(0F, 1F)
        thirdAnimator?.addUpdateListener {

            if (thirdX == targetX && thirdY == targetY) {
                thirdAnimator?.apply {
                    removeAllListeners()
                    cancel()
                }
                return@addUpdateListener
            }

            if (thirdX == mCurX && thirdY == mCurY) {
                thirdAnimator?.apply {
                    removeAllListeners()
                    cancel()
                }
                return@addUpdateListener
            }
            thirdX = (it.animatedValue as Float) * (targetX - animatorStartX) + animatorStartX
            thirdY = (it.animatedValue as Float) * (targetY - animatorStartY) + animatorStartY
            invalidate()
        }
        // todo 动画时间有待继续调整
        thirdAnimator?.run {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 125
        }

        thirdAnimator?.start()
    }

    /**
     * 拖拽结束
     */
    private fun handleDragOver() {
        mScrolling = false
        isResettingView = true
        clearStartAnimator()
        resetAnimator()
    }

    // 清除所有开始动画
    private fun clearStartAnimator() {

        firstAnimatorSet?.run {
            removeAllListeners()
            cancel()
        }

        secondAnimatorSet?.run {
            removeAllListeners()
            cancel()
        }

        thirdAnimator?.run {
            removeAllListeners()
            cancel()
        }
    }

    // 恢复原来位置动画, 基本没问题
    private fun resetAnimator() {

        val mostXValue = mCurX
        val mostYValue = mCurY
        val xValueAnimator = ValueAnimator.ofFloat(mostXValue, targetDragView.x)
        val yValueAnimator = ValueAnimator.ofFloat(mostYValue, targetDragView.y)

        xValueAnimator.addUpdateListener {
            mCurX = (it.animatedValue as Float)
            // 刷新界面
//            invalidate()
        }

        yValueAnimator.addUpdateListener {
            mCurY = (it.animatedValue as Float)
        }

        secondX = mostXValue
        secondY = mostYValue
        val secondXValueAnimator = ValueAnimator.ofFloat(mostXValue, targetDragView.x)
        val secondYValueAnimator = ValueAnimator.ofFloat(mostYValue, targetDragView.y)
        secondXValueAnimator.addUpdateListener {
            secondX = ((it.animatedValue as Float))
//            invalidate()
        }

        secondYValueAnimator.addUpdateListener {
            secondY = ((it.animatedValue as Float))
        }

        val animatorSet2 = AnimatorSet()
        animatorSet2.playTogether(secondXValueAnimator, secondYValueAnimator)
        animatorSet2.interpolator = OvershootInterpolator()
        animatorSet2.duration = 500

        // 第三个
        thirdX = mostXValue
        thirdY = mostYValue
        val thirdXValueAnimator = ValueAnimator.ofFloat(mostXValue, targetDragView.x)
        val thirdYValueAnimator = ValueAnimator.ofFloat(mostYValue, targetDragView.y)
        thirdXValueAnimator.addUpdateListener {
            thirdX = ((it.animatedValue as Float))
            invalidate()
        }

        thirdYValueAnimator.addUpdateListener {
            thirdY = ((it.animatedValue as Float))
        }

        val animatorSet3 = AnimatorSet()
        animatorSet3.playTogether(thirdXValueAnimator, thirdYValueAnimator)
        animatorSet3.interpolator = OvershootInterpolator()
        animatorSet3.duration = 700

        val animatorSet = AnimatorSet()

        animatorSet.playTogether(xValueAnimator, yValueAnimator)

        animatorSet.interpolator = OvershootInterpolator()
        animatorSet.duration = 300

        animatorSet3.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {

                isResettingView = false
                targetDragView.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        animatorSet.start()
        animatorSet2.start()
        animatorSet3.start()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (mScrolling) {
            // 绘画第二个第三个 bitmap
            canvas.drawBitmap(targetBitmap, thirdX, thirdY, secondAlphaPaint)
            canvas.drawBitmap(targetBitmap, secondX, secondY, secondAlphaPaint)

            // 第一个，最上面的 bitmap
            canvas.drawBitmap(targetBitmap, firstX, firstY, bitmapPaint)

            // 原本的代码：第一个，最上面的 bitmap
//            canvas.drawBitmap(targetBitmap, firstX, firstY, bitmapPaint)
        }

        // 恢复这里是没问题的
        if (isResettingView) {
            // 复位 view 时的操作
            // 第三个 bitmap
            canvas.drawBitmap(targetBitmap, thirdX, thirdY, secondAlphaPaint)
            // 第二个 bitmap
            canvas.drawBitmap(targetBitmap, secondX, secondY, secondAlphaPaint)
            // 第一个 bitmap
            canvas.drawBitmap(targetBitmap, mCurX, mCurY, bitmapPaint)
        }
    }


}