package com.chendroid.learning.ui.holder

import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.bean.TodoData
import com.chendroid.learning.data.usecase.TodoUseCase.Companion.TYPE_TODO_JOB
import com.chendroid.learning.data.usecase.TodoUseCase.Companion.TYPE_TODO_LEARN
import com.chendroid.learning.data.usecase.TodoUseCase.Companion.TYPE_TODO_LIFE
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.holder_todo_item.view.*

/**
 * @intro to`do 列表的 item view holder
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/22
 */
@Layout(R.layout.holder_todo_item)
class TodoListItemHolder(view: View) : SugarHolder<TodoData.TodoBaseData>(view) {

    private val likeTextView by lazy {
        itemView.like_text_view
    }

    private val titleView = itemView.item_title
    private val contentView = itemView.item_content
    private val dateView = itemView.item_date

    private val tabView = itemView.todo_tag_view


    override fun onBindData(data: TodoData.TodoBaseData) {
        setupLikeTextView()
    }

    private fun setupLikeTextView() {

        if (data.title.isNullOrEmpty()) {
            titleView.visibility = View.GONE
        } else {
            titleView.visibility = View.VISIBLE
            titleView.text = data.title
        }

        if (data.content.isNullOrEmpty()) {
            contentView.visibility = View.GONE
        } else {
            contentView.visibility = View.VISIBLE
            contentView.text = data.content
        }

        dateView.text = data.dateStr

        setupTagView()

    }

    private fun setupTagView() {
        when (data.type) {
            TYPE_TODO_JOB -> tabView.tagText = "工作"
            TYPE_TODO_LIFE -> tabView.tagText = "生活"
            TYPE_TODO_LEARN -> tabView.tagText = "学习"
            else -> tabView.tagText = "默认"
        }
    }
}