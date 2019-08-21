package com.chendroid.learning.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.ui.adapter.FirstMainPagerAdapter
import com.chendroid.learning.ui.fragment.FirstHomeFragment
import com.chendroid.learning.ui.fragment.MoreArticleTagFragment
import kotlinx.android.synthetic.main.activity_first_main.*

class FirstMainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.run { title = getString(R.string.first_activity_title) }
        initImmersionBar()
        initViewPager()

        initTabLayout()
    }

    private fun initTabLayout() {
//        main_tab_layout.tabMode = TabLayout.MODE_SCROLLABLE

        val firstHomeFragment = FirstHomeFragment()
        val moreTypeFragment = MoreArticleTagFragment()

        var tabTitleList = listOf<String>("首页", "更多")

        var fragmentList = listOf<Fragment>(firstHomeFragment, moreTypeFragment)

        main_view_pager.adapter = FirstMainPagerAdapter(tabTitleList, fragmentList, supportFragmentManager)
//        main_tab_layout.setupWithViewPager(main_view_pager)
        main_tab_layout.setupWithViewPager(main_view_pager)
        main_tab_layout.setTabIndicatorFullWidth(false)
    }

    private fun initViewPager() {

//        ApiServiceHelper


    }

    override fun setLayoutId(): Int {
        return R.layout.activity_first_main
    }

    override fun cancelRequest() {

    }
}
