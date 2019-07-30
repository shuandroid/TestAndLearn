package com.chendroid.learning.widget.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.chendroid.learning.R

/**
 * @intron  标签 View
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/7/30
 */
class TagView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        // 标签默认状态
        const val STATUE_DEFAULT = 0
        // 被选中状态
        const val STATUE_SELECTED = 1
        // 不可选中状态
        const val STATUE_UN_ENABLE = 2
        // 仅作为展示状态
        const val STATUE_JUST_SHOW = 3
        // 弹窗中的 Tag 标签样式
        const val STATUE_DIALOG_STYLE = 4

        // width 宽度上的 margin, 单位为 dp，需要转化为 px
        const val MARGIN_WIDTH = 14
        const val MARGIN_HEIGHT = 6
    }

    // tag 文本颜色
    var textColor: Int
    // tag 文本大小 , 单位为 px
    var textSize: Float
    // tag 回答数量
    var answerCount: String?
    // tag 文本
    var tagText: String?
    // tag 背景
    private var tagBackground: Int

    // tag 的状态，目前定义了几种状态
    var tagStatus = STATUE_DEFAULT

    private val tagTextView: TextView by lazy {
        TextView(context)
    }

    init {
        val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.QuestionAnswerTagViewStyle)

        tagText = typedArray.getString(R.styleable.QuestionAnswerTagViewStyle_questionTagText)
        textColor = typedArray.getColor(R.styleable.QuestionAnswerTagViewStyle_questionTextColor, context.resources.getColor(R.color.GBK05A))
        textSize = typedArray.getDimension(R.styleable.QuestionAnswerTagViewStyle_questionTextSize, 14.dp.toFloat())
        answerCount = typedArray.getString(R.styleable.QuestionAnswerTagViewStyle_questionTagCount)
        tagBackground = typedArray.getResourceId(R.styleable.QuestionAnswerTagViewStyle_questionBackground, R.drawable.question_tag_default_bg)

        tagStatus = typedArray.getInt(R.styleable.QuestionAnswerTagViewStyle_questionStatus, STATUE_DEFAULT)

        typedArray.recycle()

        setupView()
    }

    private fun setupView() {

        updateTagTextReal()

        tagTextView.setTextColor(textColor)
        tagTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        tagTextView.gravity = Gravity.CENTER

        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(MARGIN_WIDTH.dp, MARGIN_HEIGHT.dp, MARGIN_WIDTH.dp, MARGIN_HEIGHT.dp)

        addView(tagTextView, layoutParams)

        updateBackgroundReal()
    }

    /**
     * 供外部调用改变标签文本和数量， count 默认为 -1。
     */
    fun updateTagText(tagText: String, count: Long = -1) {
        this.tagText = tagText
        if (count >= 0) {
            this.answerCount = count.toString()
        }

        updateTagTextReal()
    }

    /**
     * 内部更新标签文本的方法
     */
    private fun updateTagTextReal() {
        if (TextUtils.isEmpty(answerCount) || answerCount.equals("0")) {
            tagTextView.text = tagText
        } else {
            tagTextView.text = context.getString(R.string.question_tag_text, tagText, NumberUtils.numberToKBase(answerCount!!.toInt()))
        }
    }

    /**
     * 真正更新背景的位置
     */
    private fun updateBackgroundReal() {
        background = context.getDrawable(tagBackground)

        // 因为缺少对应的色组，只能手动调整透明度
        if (tagBackground == R.drawable.question_tag_selected_bg || tagBackground == R.drawable.question_tag_dialog_selected_bg) {
            background.alpha = (0.08f * 255).toInt()
        } else if (tagBackground == R.drawable.question_tag_dialog_unavailable_bg) {
            background.alpha = (0.5f * 255).toInt()
        } else {
            background.alpha = 255
        }
    }

    /**
     * 设置 Tag 的文本颜色，效果等同于 R.styleable.QuestionAnswerTagViewStyle_questionTextColor
     * textColor 为具体的 A single color value in the form 0xAARRGGBB.
     */
    fun setTagTextColor(textColor: Int) {
        this.textColor = textColor
        tagTextView.setTextColor(textColor)
    }

    /**
     * 设置 Tag 的文本字体大小，效果等同于 R.styleable.QuestionAnswerTagViewStyle_questionTextSize
     */
    fun setTagTextSize(textSizePx: Float) {
        this.textSize = textSizePx
        tagTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }


    /**
     * 设置 Tag 的背景颜色，效果等同于 R.styleable.QuestionAnswerTagViewStyle_questionBackground
     */
    fun setTagBackGround(tagBackground: Int) {
        this.tagBackground = tagBackground
        updateBackgroundReal()
    }

    /**
     * 外部调用通过 Status 更改 TagView， status 为这次要变成的状态. 暂时只支持 STATUE_DEFAULT、STATUE_SELECTED、STATUE_JUST_SHOW
     */
    fun updateTagViewByStatus(status: Int) {
        when (status) {
            STATUE_DEFAULT -> {
                if (tagStatus == STATUE_DEFAULT) {
                    return
                }
                // 修改默认状态
                tagStatus = STATUE_DEFAULT
                setTagTextColor(context.resources.getColor(R.color.GBK05A))
                setTagBackGround(R.drawable.question_tag_default_bg)
            }
            STATUE_SELECTED -> {
                if (tagStatus == STATUE_SELECTED) {
                    return
                }
                // 修改为选中状态
                tagStatus = STATUE_SELECTED
                setTagTextColor(context.resources.getColor(R.color.GBL01A))
                setTagBackGround(R.drawable.question_tag_selected_bg)
            }

            STATUE_JUST_SHOW -> {
                if (tagStatus == STATUE_JUST_SHOW) {
                    return
                }
                // 修改为 JUST_SHOW 状态
                tagStatus = STATUE_JUST_SHOW
                setTagTextColor(context.resources.getColor(R.color.GBL05A))
                setTagBackGround(R.drawable.question_tag_more_bg)
            }
        }
    }

}