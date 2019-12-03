package com.chendroid.learning.ui.holder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.bean.BaseDatas
import com.chendroid.learning.ui.activity.ContentDetailActivity
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.layout_article_list_item.view.*

/**
 * @intro 文章列表的 item holder
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/20
 */
@Layout(R.layout.layout_article_list_item)
class HomeListItemHolder(view: View) : SugarHolder<BaseDatas>(view), View.OnClickListener {

    private val authorTextView by lazy {
        itemView.home_item_author
    }

    //    private val authorTextView : TextView by lazy { view.findViewById<TextView>(R.id.home_item_author) }
    private val articleTitleView by lazy {
        itemView.home_item_title
    }

    private val articleDateView by lazy {
        itemView.home_item_date
    }

    private val articleTypeView by lazy {
        itemView.home_item_type
    }

    private val articleLikeView by lazy {
        itemView.home_item_like
    }


    init {
        itemView.setOnClickListener(this)
    }

    override fun onBindData(data: BaseDatas) {

        Log.i("zc_test", "HomeListItemHolder onBindData()")
        authorTextView.text = data.author
        articleTitleView.text = data.title
        articleDateView.text = data.niceDate
        articleTypeView.text = data.chapterName

        if (data.collect) {
            //收藏了该文章 todo
//            articleLikeView.
        }
    }

    override fun onClick(clickView: View) {

        if (clickView === itemView) {
            // 进入具体的文章界面
            Intent(context, ContentDetailActivity::class.java).run {
                val bundle = Bundle()
                bundle.putString(Constant.CONTENT_TITLE_KEY, this@HomeListItemHolder.data.title)
                bundle.putString(Constant.CONTENT_URL_KEY, this@HomeListItemHolder.data.link)
                bundle.putInt(Constant.CONTENT_ID_KEY, this@HomeListItemHolder.data.id)
                putExtras(bundle)
                context.startActivity(this)
            }
        }
    }

}