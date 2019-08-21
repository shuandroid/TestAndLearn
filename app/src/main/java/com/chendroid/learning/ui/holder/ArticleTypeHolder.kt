package com.chendroid.learning.ui.holder

import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.bean.ArticleTagData
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.holder_article_type_layout.view.*

/**
 * @intro 文章类型的布局 holder
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
@Layout(R.layout.holder_article_type_layout)
class ArticleTypeHolder(view: View) : SugarHolder<ArticleTagData>(view) {


    private val articleTypeView by lazy {
        itemView.article_type_title
    }


    override fun onBindData(data: ArticleTagData) {

        articleTypeView.updateTagText(data.name)
    }


}