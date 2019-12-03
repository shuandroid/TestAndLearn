package com.chendroid.learning.base

import androidx.fragment.app.Fragment

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/18
 */
abstract class BaseFragment : androidx.fragment.app.Fragment() {


    protected abstract fun cancelRequest()

    override fun onDestroyView() {
        super.onDestroyView()
        cancelRequest()
    }
}