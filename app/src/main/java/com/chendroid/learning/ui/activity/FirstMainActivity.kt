package com.chendroid.learning.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.base.Preference
import com.chendroid.learning.extension.openAccount
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
        layoutInflater.toString()

        initViewPager()
        initTabLayout()

        initHomeToolbar()
        //todo 添加用户信息的网络请求

        initImmersionBar()
    }

//    override fun initImmersionBar() {
//    }

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

            setToolbarTitle("hahhaha")

            homeToolbarListener = object : HomeToolbar.HomeToolbarListener {
                override fun onAvatarViewClicked() {
                    // 打开账号界面
                    openAccount()
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
        lifecycle
    }

    override fun onResume() {
        super.onResume()

        // 测试大量写入 SharedPreference 时，会在 onPause 中等待 QueuedWork.waitToFinish()
        // 可能会产生 ANR
//        Handler().postDelayed({
//            //
//            Log.i("zc_test", "FirstMainActivity  onResume  延迟")
//
//            for (i in 0..50000) {
//                var test : String by  Preference("test_onresume", "")
//                test = "$i"
//            }
//
//        }, 1000)
    }

    override fun onPause() {
        Log.i("zc_test", "FirstMainActivity pre onPause ")
        super.onPause()
        Log.i("zc_test", "FirstMainActivity after onPause ")
    }

    override fun onStop() {
        Log.i("zc_test", "FirstMainActivity pre onStop ")
        super.onStop()
        Log.i("zc_test", "FirstMainActivity pre onStop ")

    }

    override fun onDestroy() {
        Log.i("zc_test", "FirstMainActivity pre onDestroy ")
        super.onDestroy()
        Log.i("zc_test", "FirstMainActivity after onDestroy ")
    }
}
