package com.chendroid.learning.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.ui.adapter.FirstMainPagerAdapter
import com.chendroid.learning.ui.fragment.FirstHomeFragment
import com.chendroid.learning.ui.fragment.MoreArticleTagFragment
import com.chendroid.learning.widget.view.HomeToolbar
import kotlinx.android.synthetic.main.activity_first_main.*

/**
 * 主页
 */
class FirstMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewPager()
        initTabLayout()

        initHomeToolbar()
        //todo 添加用户信息的网络请求
    }

    override fun initImmersionBar() {
    }

    private fun initTabLayout() {

        val firstHomeFragment = FirstHomeFragment()
        val moreTypeFragment = MoreArticleTagFragment()

        var tabTitleList = listOf<String>("首页", "更多")

        var fragmentList = listOf<Fragment>(firstHomeFragment, moreTypeFragment)

        main_view_pager.adapter = FirstMainPagerAdapter(tabTitleList, fragmentList, supportFragmentManager)
        main_tab_layout.setupWithViewPager(main_view_pager)
        main_tab_layout.isTabIndicatorFullWidth = false
    }

    private fun initViewPager() {

    }

    private fun initHomeToolbar() {
        val homeToolbar = home_toolbar

        homeToolbar.apply {
            //

            setToobarTitle("hahhaha")

            homeToolbarListener = object : HomeToolbar.HomeToolbarListener {
                override fun onAvatarViewClicked() {
                    // todo 跳转

                }
            }
        }
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_first_main
    }

    override fun cancelRequest() {

    }

    override fun attachBaseContext(newBase: Context?) {
        // 在这可修改 context
        super.attachBaseContext(newBase)
    }
}
