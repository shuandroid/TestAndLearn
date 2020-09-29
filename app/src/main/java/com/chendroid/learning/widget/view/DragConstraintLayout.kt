package com.chendroid.learning.widget.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.chendroid.learning.utils.ViewUtils
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @intro 可指定一个目标 view，可以实现类似拖动 view 的效果。「其实并没有拖动 view，是画出来的 bitmap」
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/7/2
 */
class DragConstraintLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    // 第一次手势 down, 按下时的 x, y 位置
    private var mTouchDownX = 0f
    private var mTouchDownY = 0f

    //  view 位置相关变量
    // 上一次的 move 事件坐标，用来检测是否为拖拽悬停状态
    private var mPrevX = 0f
    private var mPrevY = 0f
    private var mCurX = 0f
    private var mCurY = 0f

    private var mFirstEventX = 0f
    private var mFirstEventY = 0f

    // 是否在拖动的标识位
    private var mScrolling = false

    // 是否正在重设 view 位置
    private var isResettingView = false

    // 对多个手指做处理的标识位
    private var mIsCanMove = false

    // 是否为第一次移动
    private var isFirstMove = true

    private lateinit var targetDragView: View

    lateinit var targetBitmap: Bitmap

    // 绘制 bitmap 的画笔
    private var bitmapPaint: Paint

    // 第二个，第三个 bitmap 的位置
    var secondX = 0F
    var secondY = 0F
    var thirdX = 0F
    var thirdY = 0F

    // 用作
    var secondAnimator: ValueAnimator? = null
    var thirdAnimator: ValueAnimator? = null

    // 用来记录拖拽的路径，list
    private val positionXList = ArrayList<Float>()
    private val positionYList = ArrayList<Float>()

    // 第二个 bitmap 开始从 List 中取坐标的 start, end 值
    private var startSecondPosition = 0
    private var endSecondPosition = 0

    private var startThirdPosition = 0
    private var endThirdPosition = 0

    init {
        apply {
            // ConstraintLayout 默认不会拦截事件，需要设置 isClickable = true
            isClickable = true
        }

        bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {

        // down 的时候判断
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (!ViewUtils.isTouchEventInTargetView(event, targetDragView)) {
                mTouchDownX = event.x
                mTouchDownY = event.y
                return false
            }
        }

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
            }
            MotionEvent.ACTION_UP -> mScrolling = false
        }

        Log.i("zc_test", "TestConstraintLayout onInterceptTouchEvent scroll is $mScrolling")

        return mScrolling
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i("zc_test", "TestConstraintLayout onTouchEvent")
        // down 的时候判断
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (!ViewUtils.isTouchEventInTargetView(event, targetDragView)) {
                Log.i("zc_test", "TestConstraintLayout  onTouchEvent 不拦截")
                mTouchDownX = event.x
                mTouchDownY = event.y
                val result = super.onTouchEvent(event)
                Log.i("zc_test", "TestConstraintLayout onTouchEvent result is $result ")
                return result
            }
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

                if (!mIsCanMove) {
                    return@run
                }
                delta = sqrt((mTouchDownX - event.x.toDouble()).pow(2.0) + (mTouchDownY - event.y.toDouble()).pow(2.0)).toInt()
                if (delta < ViewConfiguration.get(context).scaledTouchSlop) {
                    //不满足对滑动距离的判断，则直接返回
                    return@run
                }

                if (isFirstMove) {
                    mFirstEventX = event.rawX
                    mFirstEventY = event.rawY

                    secondX = targetDragView.x
                    secondY = targetDragView.y
                    thirdX = targetDragView.x
                    thirdY = targetDragView.y
                    mCurX = targetDragView.x
                    mCurY = targetDragView.y
                    isFirstMove = false
                }

                positionXList.add(mCurX)
                positionYList.add(mCurY)

                val deltaX = event.rawX - mFirstEventX
                val deltaY = event.rawY - mFirstEventY

                mPrevX = mCurX
                mPrevY = mCurY

                mCurX = targetDragView.x + deltaX
                mCurY = targetDragView.y + deltaY

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

    /**
     * 设置目标 view , 并从中获取到 bitmap
     */
    fun setTargetView(view: View) {
        targetDragView = view

        if (targetDragView.visibility == View.GONE) {
            return
        }

        targetDragView.post {
            ViewUtils.fetchBitmapFromView(targetDragView, (context as Activity).window ) { bitmap ->
                setTestTargetBitmap(bitmap)
            }
        }
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




        // 设置它为不可见
        targetDragView.visibility = View.INVISIBLE
        mScrolling = true

        if (mCurX == mPrevX && mCurY == mPrevY) {
            // 悬停时，不刷新
            return
        }

        // 如果没有坐标，则返回
        if (positionXList.size == 0) {
            return
        }

        calculateSecondBitmapPosition()
        calculateThirdBitmapPosition()
    }

    // todo 需要找个时机，当悬停时，需要清空 list , 丢弃一些历史数据；
    private fun clearAndUpdateList() {
        Log.i("TestConstraintLayout", "clearAndUpdateList()")
        positionXList.clear()
        positionYList.clear()
        positionXList.add(mCurX)
        positionYList.add(mCurY)
    }

    private fun calculateSecondBitmapPosition() {
        // 使用动画
        secondAnimator?.run {
            // 如果正在运行，则不影响它的运行，下次再开启动画
            if (isRunning) {
                return
            }

            removeAllListeners()
            cancel()
        }

        endSecondPosition = positionXList.size
        // 这样不和谐
        val targetX = mCurX
        val targetY = mCurY
        secondAnimator = ValueAnimator.ofInt(startSecondPosition, endSecondPosition - 1)
        secondAnimator?.addUpdateListener {

            if (secondX == targetX && secondY == targetY) {
                secondAnimator?.apply {
                    removeAllListeners()
                    cancel()
                }
                return@addUpdateListener
            }

            if (positionXList.size < it.animatedValue as Int) {
                secondAnimator?.apply {
                    removeAllListeners()
                    cancel()
                }
                return@addUpdateListener
            }

            secondX = positionXList[it.animatedValue as Int]
            secondY = positionYList[it.animatedValue as Int]
        }

        secondAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                startSecondPosition = endSecondPosition
            }

            override fun onAnimationCancel(animation: Animator?) {
                startSecondPosition = endSecondPosition
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        secondAnimator?.apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 75
        }?.start()
    }

    private fun calculateThirdBitmapPosition() {
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
        endThirdPosition = positionXList.size
        thirdAnimator = ValueAnimator.ofInt(startThirdPosition, endThirdPosition - 1)
        thirdAnimator?.addUpdateListener {

            if (thirdX == targetX && thirdY == targetY) {
                thirdAnimator?.apply {
                    removeAllListeners()
                    cancel()
                }
                return@addUpdateListener
            }

            if (it.animatedValue as Int >= positionXList.size) {
                thirdAnimator?.apply {
                    removeAllListeners()
                    cancel()
                }
                return@addUpdateListener
            }

            thirdX = positionXList[it.animatedValue as Int]
            thirdY = positionYList[it.animatedValue as Int]
            invalidate()
            requestLayout()
        }

        thirdAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                startThirdPosition = endThirdPosition
            }

            override fun onAnimationCancel(animation: Animator?) {
                startThirdPosition = endThirdPosition
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        thirdAnimator?.apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 125
        }?.start()

        thirdAnimator?.start()
    }

    /**
     * 拖拽结束
     */
    private fun handleDragOver() {
        // 如果不是在滑动中，则不需要执行后续操作
        if (!mScrolling) {
            return
        }

        mScrolling = false
        isResettingView = true
        clearDragingAnimator()
        startResetAnimator()
    }

    // 清除所有开始动画
    private fun clearDragingAnimator() {
        secondAnimator?.run {
            removeAllListeners()
            cancel()
        }

        thirdAnimator?.run {
            removeAllListeners()
            cancel()
        }
    }

    // 恢复原来位置动画, 基本没问题
    private fun startResetAnimator() {

        val startXValue = mCurX
        val startYValue = mCurY

        val offsetX = targetDragView.x - mCurX
        val offsetY = targetDragView.y - mCurY

        val firstValueAnimator = ValueAnimator.ofFloat(0F, 1F)

        firstValueAnimator.addUpdateListener {
            mCurX = (it.animatedValue as Float) * offsetX + startXValue
            mCurY = (it.animatedValue as Float) * offsetY + startYValue
        }

        firstValueAnimator.apply {
            interpolator = OvershootInterpolator()
            duration = 300
        }
        // 第二个
        val secondValueAnimator = ValueAnimator.ofFloat(0F, 1F)
        secondValueAnimator.apply {
            interpolator = OvershootInterpolator()
            duration = 500
        }

        secondValueAnimator.addUpdateListener {
            secondX = (it.animatedValue as Float) * offsetX + startXValue
            secondY = (it.animatedValue as Float) * offsetY + startYValue
        }

        // 第三个
        val thirdValueAnimator = ValueAnimator.ofFloat(0F, 1F)
        thirdValueAnimator.apply {
            interpolator = OvershootInterpolator()
            duration = 700
        }
        thirdValueAnimator.addUpdateListener {
            thirdX = (it.animatedValue as Float) * offsetX + startXValue
            thirdY = (it.animatedValue as Float) * offsetY + startYValue
            invalidate()
            requestLayout()
        }

        thirdValueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                resetData()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        firstValueAnimator.start()
        secondValueAnimator.start()
        thirdValueAnimator.start()

        postDelayed({
            // 延迟 100 ms 后进行震动
            startVibrateEffect()
        }, 100)
    }

    /**
     * 重设各个变量的初始值
     */
    private fun resetData() {
        isResettingView = false
        targetDragView.visibility = View.VISIBLE
        mCurX = targetDragView.x
        mCurY = targetDragView.y
        startSecondPosition = 0
        startThirdPosition = 0
        endSecondPosition = 0
        endThirdPosition = 0
        positionXList.clear()
        positionYList.clear()
    }

    // 在这里绘制 bitmap
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (mScrolling) {
            // 绘画第二个第三个 bitmap
            bitmapPaint.alpha = (255 * (0.65)).toInt()
            canvas.drawBitmap(targetBitmap, thirdX, thirdY, bitmapPaint)
            bitmapPaint.alpha = (255 * (0.85)).toInt()
            canvas.drawBitmap(targetBitmap, secondX, secondY, bitmapPaint)
            // 第一个，最上面的 bitmap
            bitmapPaint.alpha = 255
            canvas.drawBitmap(targetBitmap, mCurX, mCurY, bitmapPaint)
        }

        // 复位 view 时的操作
        if (isResettingView) {
            // 第三个 bitmap
            bitmapPaint.alpha = (255 * (0.65)).toInt()
            canvas.drawBitmap(targetBitmap, thirdX, thirdY, bitmapPaint)
            // 第二个 bitmap
            bitmapPaint.alpha = (255 * (0.85)).toInt()
            canvas.drawBitmap(targetBitmap, secondX, secondY, bitmapPaint)
            // 第一个 bitmap
            bitmapPaint.alpha = 255
            canvas.drawBitmap(targetBitmap, mCurX, mCurY, bitmapPaint)
        }
    }

    // 开启震动效果
    private fun startVibrateEffect() {
        if (context == null) {
            return
        }

        val vibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

        // 需要添加权限  uses-permission
        vibrator.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate(VibrationEffect.createOneShot(30L, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrate(30L)
            }
        }
    }

}