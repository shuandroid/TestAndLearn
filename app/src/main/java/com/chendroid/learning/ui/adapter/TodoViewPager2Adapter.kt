package com.chendroid.learning.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @intro
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/25
 */
class TodoViewPager2Adapter(fragmentActivity: FragmentActivity, var fragmentList: List<Fragment>) : FragmentStateAdapter(fragmentActivity) {
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    /**
     * Provide a new Fragment associated with the specified position.
     *
     * The adapter will be responsible for the Fragment lifecycle:
     *
     *  * The Fragment will be used to display an item.
     *  * The Fragment will be destroyed when it gets too far from the viewport, and its state
     * will be saved. When the item is close to the viewport again, a new Fragment will be
     * requested, and a previously saved state will be used to initialize it.
     *
     * @see ViewPager2.setOffscreenPageLimit
     */
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}