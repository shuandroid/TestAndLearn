package com.chendroid.learning.base

import android.support.v4.app.Fragment

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/18
 */
abstract class BaseFragment : Fragment() {


    protected abstract fun cancelRequest()

    override fun onDestroyView() {
        super.onDestroyView()
        cancelRequest()
    }
}