package com.chendroid.learning.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.ui.adapter.TodoViewPager2Adapter
import com.chendroid.learning.ui.fragment.DetailTodoFinishedFragment
import com.chendroid.learning.ui.fragment.DetailTodoDoingFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_todo.*

/**
 * @intro 我的 `Todo` 界面
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/15
 */
class TodoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var fabTodoView: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        initView()
        initToolbar()
        initViewPager()
    }

    private fun initView() {
        fabTodoView = add_todo_button

        fabTodoView.setOnClickListener {
            // 点击新增。 todo 增加 transform 动画
        }
    }

    /**
     * 初始化 toolbar 相关信息
     */
    private fun initToolbar() {
        toolbar = todo_toolbar
        toolbar.title = "ToDo 列表"
        setSupportActionBar(toolbar)
    }

    private fun initViewPager() {
        //
        val tabTitleList = listOf("Todo", "Done")

        val todoTypeFragment1 = DetailTodoDoingFragment()
        val todoTypeFragment2 = DetailTodoFinishedFragment()

        val fragmentList = listOf<Fragment>(todoTypeFragment1, todoTypeFragment2)

        todo_view_pager.adapter = TodoViewPager2Adapter(this, fragmentList)

        TabLayoutMediator(todo_tab_layout, todo_view_pager) { tab, position -> tab.text = tabTitleList[position] }
                .attach()

        todo_tab_layout.isTabIndicatorFullWidth = false
    }



}