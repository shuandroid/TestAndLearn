package com.chendroid.learning.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chendroid.learning.R
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.layout_home_toolbar.view.*

/**
 * @intro 首页的 toobar 定义布局
 * @author zhaochen@ZhiHu Inc.
 * @since 2020-01-13
 */
class HomeToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    /**
     * 对外暴露的接口
     */
    interface HomeToolbarListener {
        fun onAvatarViewClicked()
    }


    /**
     * 头像 view
     */
    private var avatarImageView: SimpleDraweeView

    /**
     * 标题
     */
    private var toolbarTitle: TextView

    var homeToolbarListener: HomeToolbarListener? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_home_toolbar, this, true)

        avatarImageView = view.user_avatar
        toolbarTitle = view.toolbar_title_text
        avatarImageView.setOnClickListener(this)
    }

    /**
     * 外部设置头像
     */
    fun setAvatarImage(imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            avatarImageView.setImageURI(imageUrl)
        }
    }

    fun setToolbarTitle(title: String) {
        toolbarTitle.text = title
    }

    override fun onClick(clickedView: View) {

        if (clickedView === avatarImageView) {
            homeToolbarListener?.run {
                onAvatarViewClicked()
            }
        }

    }


}