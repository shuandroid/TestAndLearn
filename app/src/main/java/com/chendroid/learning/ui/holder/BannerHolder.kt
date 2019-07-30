package com.chendroid.learning.ui.holder

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chendroid.learning.R
import com.chendroid.learning.R2
import com.chendroid.learning.bean.HomeBanner
import com.zhihu.android.sugaradapter.Id
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/22
 */
@Layout(R.layout.holder_first_banner_home_layout)
class BannerHolder(view: View) : SugarHolder<HomeBanner.BannerItemData>(view) {

    private val imageView: ImageView  by lazy { view.findViewById<ImageView>(R.id.home_banner_image) }

    override fun onBindData(data: HomeBanner.BannerItemData) {
        Log.e("zc_test", "BannerHolder  onBindData")
        if (!TextUtils.isEmpty(data.imagePath)) {
            Glide.with(context).load(data.imagePath).into(imageView)
        }

    }
}