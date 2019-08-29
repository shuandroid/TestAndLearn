package com.chendroid.learning.ui.holder

import android.graphics.Paint
import android.util.Log
import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.ui.holder.data.ArticleTagDataWrapper
import com.chendroid.learning.ui.holder.data.MoreTagData
import com.google.android.flexbox.*
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarAdapter
import com.zhihu.android.sugaradapter.SugarHolder
import dp
import kotlinx.android.synthetic.main.holder_article_type_layout.view.*

/**
 * @intro 文章类型的布局 holder
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
@Layout(R.layout.holder_article_type_layout)
class ArticleTypeHolder(view: View) : SugarHolder<ArticleTagDataWrapper>(view), TagInnerMoreHolder.MoreTagListener {

    private val articleTypeView by lazy {
        itemView.article_type_title
    }

    private val articleTagRecyclerView by lazy {
        itemView.article_tag_recycler_view
    }

    private val tagsList: MutableList<Any> = mutableListOf()

    private val tagsItemBuilder by lazy {
        SugarAdapter.Builder.with(tagsList)
    }

    private val tagsItemAdapter by lazy {
        tagsItemBuilder.build()
    }

    init {

        initAdapter()
        initRecyclerView()
    }

    override fun onBindData(data: ArticleTagDataWrapper) {

        articleTypeView.text = data.articleTagData.name

        updateArticleTagList()
    }

    private fun initAdapter() {
        tagsItemBuilder.add(TagInnerHolder::class.java)
                .add(TagInnerMoreHolder::class.java) { holder -> holder.moreTagListener = this }
    }

    private fun initRecyclerView() {

        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.apply {
            // 正常换行
            flexWrap = FlexWrap.WRAP
            // 主轴方向，默认值，左端
            flexDirection = FlexDirection.ROW
            //交叉轴的起点对齐
            alignItems = AlignItems.FLEX_START
            //对齐方式，左对齐
            justifyContent = JustifyContent.FLEX_START
        }

        articleTagRecyclerView.apply {
            isNestedScrollingEnabled = false
            this.layoutManager = layoutManager
            adapter = tagsItemAdapter
        }
    }

    private fun updateArticleTagList() {

        if (data.articleTagData.children == null) {
            return
        }

        data.articleTagData.children?.run {

            tagsList.clear()

            if (itemView.tag == adapterPosition) {
                Log.i("zc_test", "enenne =====")
            } else {
                Log.i("zc_test", "enenne !!!!!!!!=====")
            }

            itemView.tag = adapterPosition

            if (data.showAllTag) {
                tagsList.addAll(this)
                tagsItemAdapter.notifyDataSetChanged()
            } else {
                calculateMoreTagPosition()
            }
        }
    }

    /**
     * 计算在何处插入 AnswerMoreTag
     */
    private fun calculateMoreTagPosition() {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val marginWidth = 32.dp
        // 每个 AnswerTagView 有的 marginEnd 8dp
        val itemMarginEnd = 8.dp
        // 字体距离 AnswerTagView 的左右边距「14 * 2」
        val itemMargin = 28.dp
        val paint = Paint()
        // 字体的大小是和 AnswerTagView 的字体大小一致的, 这里写死了代码，不是很友好。
        paint.textSize = 14.dp.toFloat()
        // 利用画笔测试文字的宽度
        var allTagWidth = paint.measureText((data.articleTagData.children!!.size).toString() + "+").toInt()

        allTagWidth += itemMargin + itemMarginEnd

        var indexList = -1
        for (i in 0 until data.articleTagData.children!!.size) {
            allTagWidth += paint.measureText(data.articleTagData.children!![i].name).toInt()
            allTagWidth += itemMarginEnd + itemMargin

            if (allTagWidth > screenWidth - marginWidth) {
                indexList = i
                break
            }
        }

        //检索到的 index 1. 要 >= 0  2. 要 <   data.data.size - 1 保证不是最后一个位置 3. 如果被选中的最后一个标签的位置，不在 indexList 里面需要展开全部的标签
        if (indexList >= 0 && indexList < data.articleTagData.children!!.size - 1) {
            tagsList.addAll(data.articleTagData.children!!.subList(0, indexList))
            tagsList.add(indexList, MoreTagData((data.articleTagData.children!!.size - indexList).toString() + "+"))
        } else {
            //加入全部的数据，展示全部标签
            tagsList.addAll(data.articleTagData.children!!)
        }

        tagsItemAdapter.notifyDataSetChanged()
    }

    /**
     * 当「更多标签」被点击时
     */
    override fun onMoreTagViewClicked() {

        data.showAllTag = true
        tagsList.clear()

        tagsList.addAll(data.articleTagData.children!!)
        tagsItemAdapter.notifyDataSetChanged()
    }
}