package com.chendroid.learning.ui.holder.data

import com.chendroid.learning.bean.ArticleTagData

/**
 * @intro ArticleTagData 数据的包装类，用作 Holder 的解析
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-29
 */
data class ArticleTagDataWrapper(
        // 原网络解析到的数据
        val articleTagData: ArticleTagData,
        //是否需要展示全部的标签 boolean
        var showAllTag: Boolean = false)