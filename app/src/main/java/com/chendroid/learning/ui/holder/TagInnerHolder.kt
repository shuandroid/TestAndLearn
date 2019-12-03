package com.chendroid.learning.ui.holder

import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.bean.ChildrenTagData
import com.chendroid.learning.widget.view.TagView
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.tag_inner_layout.view.*

/**
 * @intro 文章内部的分类标签布局 holder
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
@Layout(R.layout.tag_inner_layout)
class TagInnerHolder(view: View) : SugarHolder<ChildrenTagData>(view) {

    private val articleTagView by lazy {
        itemView.article_tag_view
    }

    override fun onBindData(data: ChildrenTagData) {

        articleTagView.tagText = data.name
        articleTagView.updateTagViewByStatus(TagView.STATUE_SELECTED)
    }
}