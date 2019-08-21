package com.chendroid.learning.ui.holder

import android.graphics.Paint
import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.bean.ArticleTagData
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
class ArticleTypeHolder(view: View) : SugarHolder<ArticleTagData>(view), TagInnerMoreHolder.MoreTagListener {


    private val articleTypeView by lazy {
        itemView.article_type_title
    }

    private val articleTagRecyclerView by lazy {
        itemView.article_tag_recycler_view
    }

    private val tagsList: MutableList<Any> = mutableListOf()

    // 是否需要展示所有的 tag，外部 fragment 可控制。目前逻辑：一旦展开后，不支持折叠。
    var needShowAllTag = false

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

    override fun onBindData(data: ArticleTagData) {

        articleTypeView.text = data.name

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

        if (data.children == null) {
            return
        }

        data.children?.run {

            tagsList.clear()
            if (needShowAllTag) {
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
        var allTagWidth = paint.measureText((data.children!!.size).toString() + "+").toInt()

        allTagWidth += itemMargin + itemMarginEnd

        var indexList = -1
        for (i in 0 until data.children!!.size) {
            allTagWidth += paint.measureText(data.children!![i].name).toInt()
            allTagWidth += itemMarginEnd + itemMargin

            if (allTagWidth > screenWidth - marginWidth) {
                indexList = i
                break
            }
        }

        //检索到的 index 1. 要 >= 0  2. 要 <   data.data.size - 1 保证不是最后一个位置 3. 如果被选中的最后一个标签的位置，不在 indexList 里面需要展开全部的标签
        if (indexList >= 0 && indexList < data.children!!.size - 1) {
            tagsList.addAll(data.children!!.subList(0, indexList))
            tagsList.add(indexList, MoreTagData((data.children!!.size - indexList).toString() + "+"))
        } else {
            //加入全部的数据，展示全部标签
            tagsList.addAll(data.children!!)
        }

        tagsItemAdapter.notifyDataSetChanged()
    }

    /**
     * 当「更多标签」被点击时
     */
    override fun onMoreTagViewClicked() {

        needShowAllTag = true
        tagsList.clear()

        tagsList.addAll(data.children!!)
        tagsItemAdapter.notifyDataSetChanged()
    }
}