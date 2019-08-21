package com.chendroid.learning.ui.holder

import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.ui.holder.data.MoreTagData
import com.chendroid.learning.widget.view.TagView
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.tag_inner_layout.view.*

/**
 * @intro
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
@Layout(R.layout.tag_inner_layout)
class TagInnerMoreHolder(view: View) : SugarHolder<MoreTagData>(view), View.OnClickListener {


    /**
     *  更多标签被点击的回调
     */
    interface MoreTagListener {
        fun onMoreTagViewClicked()
    }

    var moreTagListener: MoreTagListener? = null

    private val moreTagView by lazy {
        itemView.article_tag_view
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onBindData(data: MoreTagData) {

        moreTagView.tagText = data.moreText
        moreTagView.updateTagViewByStatus(TagView.STATUE_JUST_SHOW)

    }

    override fun onClick(clickedView: View) {

        moreTagListener?.run {
            onMoreTagViewClicked()
        }
    }
}