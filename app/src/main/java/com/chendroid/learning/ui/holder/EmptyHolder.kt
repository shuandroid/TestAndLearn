package com.chendroid.learning.ui.holder

import android.view.View
import com.chendroid.learning.R
import com.chendroid.learning.ui.holder.data.EmptyData
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder

/**
 * @intro 空界面
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-03
 */
@Layout(R.layout.holder_empty_layout)
class EmptyHolder(view: View) : SugarHolder<EmptyData>(view) {

    override fun onBindData(data: EmptyData) {
        // do nothing
    }

}