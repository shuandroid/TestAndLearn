package com.chendroid.learning.ui.holder

import Constant
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.chendroid.learning.R
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.ui.activity.ContentDetailActivity
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.holder_first_banner_home_layout.view.*

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/4/22
 */
@Layout(R.layout.holder_first_banner_home_layout)
class BannerHolder(view: View) : SugarHolder<HomeBanner.BannerItemData>(view), View.OnClickListener {

    private val imageView by lazy {
        itemView.home_banner_image
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onBindData(data: HomeBanner.BannerItemData) {
        if (!TextUtils.isEmpty(data.imagePath)) {
            Glide.with(context).load(data.imagePath).into(imageView)
        }

    }

    override fun onClick(clickView: View) {

        if (clickView === itemView) {
            // 进入具体的文章界面
            Intent(context, ContentDetailActivity::class.java).run {
                val bundle = Bundle()
                bundle.putString(Constant.CONTENT_TITLE_KEY, this@BannerHolder.data.title)
                bundle.putString(Constant.CONTENT_URL_KEY, this@BannerHolder.data.url)
                bundle.putInt(Constant.CONTENT_ID_KEY, this@BannerHolder.data.id)
                putExtras(bundle)
                context.startActivity(this)
            }
        }
    }
}