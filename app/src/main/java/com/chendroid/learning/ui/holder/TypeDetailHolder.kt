package com.chendroid.learning.ui.holder

import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.ui.holder.data.ArticleBaseDataWrapper
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import dp
import kotlinx.android.synthetic.main.holder_type_detail_layout.view.*

/**
 * @intro 文章详情列表 holder， 瀑布流效果实现
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-09-02
 */
@Layout(R.layout.holder_type_detail_layout)
class TypeDetailHolder(view: View) : SugarHolder<ArticleBaseDataWrapper>(view), View.OnClickListener {


    private val articleImage by lazy {
        itemView.article_picture
    }

    private val articleContent by lazy {
        itemView.article_content
    }

    override fun onBindData(data: ArticleBaseDataWrapper) {

        if (adapterPosition % 2 == 0) {
            articleImage.layoutParams.height = 120.dp
        } else {
            articleImage.layoutParams.height = 130.dp
        }

        articleContent.text = data.articleBaseData.title
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(clickedView: View) {

    }
}