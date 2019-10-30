package com.chendroid.learning.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/22
 */
class FirstMainPagerAdapter(var titleList: List<String>, var fragmentList: List<Fragment>, fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}