package com.chendroid.learning.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseFragment
import com.chendroid.learning.bean.TodoData
import com.chendroid.learning.ui.holder.TodoListItemHolder
import com.chendroid.learning.vm.TodoViewModel
import com.chendroid.learning.widget.view.CustomItemDecoration
import com.zhihu.android.sugaradapter.SugarAdapter
import kotlinx.android.synthetic.main.fragment_todo_detail_layout.view.*

/**
 * @intro
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/15
 */
class DetailTodoDoingFragment : BaseFragment() {


    private lateinit var recyclerView: RecyclerView

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var type1: TextView
    private lateinit var type2: TextView
    private lateinit var type3: TextView
    private lateinit var type4: TextView


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

        type1 = view.todo_type_1
        type1.apply {
            background.alpha = (0.08 * 255).toInt()
            setOnClickListener {
                handleTypeViewClick(0)
            }
        }

        type2 = view.todo_type_2
        type2.apply {
            background.alpha = (0.08 * 255).toInt()
            setOnClickListener {
                handleTypeViewClick(1)
            }
        }

        type3 = view.todo_type_3
        type3.apply {
            background.alpha = (0.08 * 255).toInt()
            setOnClickListener {
                handleTypeViewClick(2)
            }
        }

        type4 = view.todo_type_4
        type4.apply {
            background.alpha = (0.08 * 255).toInt()
            setOnClickListener {
                handleTypeViewClick(3)
            }
        }
    }

    /**
     * 处理 type 种类按钮被点击
     */
    private fun handleTypeViewClick(typeNumber: Int) {
        when (typeNumber) {
            0 -> {
                makeViewSelected(type1)
                makeViewNormal(type2)
                makeViewNormal(type3)
                makeViewNormal(type4)
            }
            1 -> {
                makeViewSelected(type2)
                makeViewNormal(type1)
                makeViewNormal(type3)
                makeViewNormal(type4)

                todoViewModel.addNewTodo()
            }
            2 -> {
                makeViewSelected(type3)
                makeViewNormal(type2)
                makeViewNormal(type1)
                makeViewNormal(type4)
            }
            3 -> {
                makeViewSelected(type4)
                makeViewNormal(type2)
                makeViewNormal(type3)
                makeViewNormal(type1)
            }
        }
        reloadDataByType(typeNumber)
    }

    /**
     * 设置成正常状态
     */
    private fun makeViewNormal(view: TextView) {
        view.apply {
            background.alpha = (0.08 * 255).toInt()
            setTextColor(resources.getColor(R.color.GBL01A))
            isEnabled = true
        }
    }

    /**
     * 设置成被选中状态
     */
    private fun makeViewSelected(view: TextView) {
        view.apply {
            background.alpha = 255
            setTextColor(resources.getColor(R.color.white))
            // 防止多次点击
            isEnabled = false
        }
    }

    private fun setupRecyclerView(view: View) {

        swipeRefresh = view.todo_swipe_refresh

        recyclerView = view.todo_recycler_view


        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = this@DetailTodoDoingFragment.adapter
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
            swipeRefresh.isRefreshing = false
        })
    }

    private fun loadData() {

        swipeRefresh.isRefreshing = true
        todoList.clear()
        todoViewModel.getDoingTodoList(1)
    }

    private fun reloadDataByType(typeNumber: Int) {
        swipeRefresh.isRefreshing = true
        todoList.clear()
        todoViewModel.getDoingTodoListByType(1, typeNumber)
    }

}