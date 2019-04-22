package com.chendroid.learning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseFragment

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/18
 */
class FirstHomeFragment : BaseFragment() {

    override fun cancelRequest() {
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_first_home_layout, container, false)
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

}