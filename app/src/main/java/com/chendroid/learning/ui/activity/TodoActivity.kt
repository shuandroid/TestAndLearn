package com.chendroid.learning.ui.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import kotlinx.android.synthetic.main.activity_todo.*

/**
 * @intro 我的 `Todo` 界面
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/15
 */
class TodoActivity: BaseActivity() {


    private lateinit var toolbar: Toolbar



    override fun setLayoutId(): Int {
        return R.layout.activity_todo
    }

    override fun cancelRequest() {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        initViewPager()
    }

    /**
     * 初始化 toolbar 相关信息
     */
    private fun initToolbar() {
        toolbar = todo_toolbar
        toolbar.title = "账户"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setOnClickListener {
            finish()
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initViewPager() {

        val tabTitleList = listOf<String>("只用这一个", "工作", "学习", "生活")


    }


}