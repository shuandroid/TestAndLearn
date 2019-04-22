package com.chendroid.learning.bean

/**
 * @intro 首页 banner 数据结构类
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/1/18
 */
data class HomeBanner(

        var bannerData: List<BannerItemData>?,
        var errorCode: Int,
        var errorMsg: String?

) {

    data class BannerItemData(
            var id: Int,
            var url: String,
            var imagePath: String,
            var title: String,
            var desc: String,
            var isVisible: Int,
            var order: Int,
            var type: Int
    )

}