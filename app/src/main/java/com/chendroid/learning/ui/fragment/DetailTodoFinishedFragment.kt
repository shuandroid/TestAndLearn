package com.chendroid.learning.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseFragment
import com.chendroid.learning.bean.TodoData
import com.chendroid.learning.ui.holder.TodoListItemHolder
import com.chendroid.learning.vm.TodoViewModel
import com.chendroid.learning.widget.view.CustomItemDecoration
import com.zhihu.android.sugaradapter.SugarAdapter
import kotlinx.android.synthetic.main.fragment_todo_detail_layout.view.*

/**
 * @intro 完成的 to`do Fragment
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/15
 */
class DetailTodoFinishedFragment : BaseFragment() {


    private lateinit var recyclerView: RecyclerView

    // 数据源列表
    private val todoList = mutableListOf<TodoData.TodoBaseData>()

    private val listHolderBuilder: SugarAdapter.Builder = SugarAdapter.Builder.with(todoList).apply {
        add(TodoListItemHolder::class.java)
    }

    private val adapter = listHolderBuilder.build()


    // viewModel
    private lateinit var todoViewModel: TodoViewModel

    override fun cancelRequest() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_todo_detail_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTypeView(view)
        setupRecyclerView(view)
        bindViewModel()
        loadData()
    }

    private fun setupTypeView(view: View) {

        val type1 = view.todo_type_1
        type1.apply {
            background.alpha = (0.08 * 255).toInt()
        }

        val type2 = view.todo_type_2
        type2.apply {
            background.alpha = (0.08 * 255).toInt()
        }


        val type3 = view.todo_type_3
        type3.apply {
            background.alpha = (0.08 * 255).toInt()
        }

        val type4 = view.todo_type_4
        type4.apply {
            background.alpha = (0.08 * 255).toInt()
        }

    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.todo_recycler_view


        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = this@DetailTodoFinishedFragment.adapter
            // 设置 recyclerView item 分割条
            addItemDecoration(CustomItemDecoration.with(context).apply { isFirstShowDecoration = true })
        }
    }

    private fun bindViewModel() {

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        todoViewModel.todoListLiveData.observe(viewLifecycleOwner, Observer<List<TodoData.TodoBaseData>> {
            Log.i("zc_test", "todoViewModel.todoListLiveData 成功")
            todoList.addAll(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun loadData() {

        todoList.clear()
        todoViewModel.getDoneTodoList(1)
    }

}