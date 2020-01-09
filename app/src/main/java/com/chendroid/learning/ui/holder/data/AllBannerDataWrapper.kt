package com.chendroid.learning.ui.holder.data

import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.ui.holder.EmptyBannerData

/**
 * @intro 承载所有 banner 数据信息的包装类
 * @author zhaochen@ZhiHu Inc.
 * @since 2020-01-09
 */
data class AllBannerDataWrapper(var bannerList: List<HomeBanner.BannerItemData>? = null,
                                var emptyData: EmptyBannerData? = null,
                                var type: String)