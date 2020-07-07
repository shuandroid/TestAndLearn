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
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.chendroid.learning.R
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

    lateinit var targetCanvas: Canvas

    private val xmode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    var secondAnimatorSet: AnimatorSet? = null

//    var secondValueAnimator: ValueAnimator? = null

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
                if (count % 8 == 0) {
                    mPrevX = mCurX
                    mPrevY = mCurY
                }
                count++
                mCurX = mFirstX + deltaX
                mCurY = mFirstY + deltaY

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

//        Log.i("zc_test", "handleDragMoving 速度为：$xEventVelocity, and y is $yEventVelocity")
        targetDragView.visibility = View.INVISIBLE

//        postDelayed({
//            secondX = mCurX
//            secondY = mCurY
//        }, 200)
//
//        postDelayed({
//            thirdX = mCurX
//            thirdY = mCurY
//        }, 400)

        if (secondAnimatorSet != null && secondAnimatorSet!!.isRunning) {
            return
        }

//        calculateSecondPosition()
        invalidate()
    }

    // 计算第二个的位置
    private fun calculateSecondPosition() {
        // 使用动画
        secondAnimatorSet?.run {
            // 如果正在运行，则不影响它的运行，下次再开启动画
            if (isRunning) {
                return
            }

            removeAllListeners()
            cancel()
        }

        secondAnimatorSet = AnimatorSet()

        val xSecondValueAnimator = ValueAnimator.ofFloat(0F, 1F)
        val ySecondValueAnimator = ValueAnimator.ofFloat(0F, 1F)

        xSecondValueAnimator?.addUpdateListener {
            Log.i("zc_test", "calculateSecondPosition 这里 second is $secondX, and value is ${it.animatedValue as Float}")
            moveValue = (it.animatedValue as Float)
//            secondX += (it.animatedValue as Float) * (mCurX - secondX)
            invalidate()
        }

        ySecondValueAnimator?.addUpdateListener {
//            secondY += (it.animatedValue as Float) * (mCurY - secondY)
        }

        secondAnimatorSet?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
                moveValue = 0f
            }
        })

        secondAnimatorSet?.run {
            playTogether(xSecondValueAnimator, ySecondValueAnimator)
            duration = 500
        }

        secondAnimatorSet?.start()
    }

    /**
     * 拖拽结束
     */
    private fun handleDragOver() {
        mScrolling = false
        isResettingView = true
        resetAnimator()
    }

    // 恢复原来位置动画
    private fun resetAnimator() {

        val mostXValue = mCurX
        val mostYValue = mCurY
        val xValueAnimator = ValueAnimator.ofFloat(mostXValue, targetDragView.x)
        val yValueAnimator = ValueAnimator.ofFloat(mostYValue, targetDragView.y)

        xValueAnimator.addUpdateListener {
            mCurX = (it.animatedValue as Float)
            // 刷新界面
            invalidate()
        }

        yValueAnimator.addUpdateListener {
            mCurY = (it.animatedValue as Float)
        }

        val animatorSet = AnimatorSet()

        animatorSet.playTogether(xValueAnimator, yValueAnimator)
        animatorSet.interpolator = OvershootInterpolator()
        animatorSet.duration = 300

        animatorSet.addListener(object : Animator.AnimatorListener {
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
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (mScrolling) {
//            Log.i("zc_test", "TestConstraintLayout dispatchDraw here here scrolling")
//            canvas.drawBitmap(targetBitmap, thirdX, thirdY, secondAlphaPaint)

            Log.i("zc_test", "TestConstraintLayout dispatchDraw secondX is $secondX, and y is $secondY， and curx is $mCurX, and curY is $mCurY")

//            canvas.drawBitmap(targetBitmap, secondX , secondY, secondAlphaPaint)

//            canvas.drawBitmap(targetBitmap, (mPrevX + (mCurX - mPrevX) * 0.7).toFloat(), (mPrevY + (mCurY - mPrevY) * 0.7).toFloat(), secondAlphaPaint)

            // todo 最好的方式是 第二个 bitmap 的位置，是逐渐变化的，而不是跳跃式变化
//            canvas.drawBitmap(targetBitmap, (mPrevX + (mCurX - mPrevX) * moveValue).toFloat(), (mPrevY + (mCurY - mPrevY) * moveValue).toFloat(), secondAlphaPaint)

            // 绘画第二个第三个 bitmap

            // 第一个，最上面的 bitmap
            canvas.drawBitmap(targetBitmap, mCurX, mCurY, bitmapPaint)
        }

        if (isResettingView) {
            // 复位 view 时的操作
            canvas.drawBitmap(targetBitmap, mCurX, mCurY, bitmapPaint)
        }

    }


}