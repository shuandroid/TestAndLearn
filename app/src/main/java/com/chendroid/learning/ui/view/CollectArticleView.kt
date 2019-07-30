package com.chendroid.learning.ui.view

import com.chendroid.learning.bean.HomeListResponse

/**
 * @intro 收集文章页面
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
interface CollectArticleView {


    fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean)


    fun collectArticleFailed(errorMessage: String?, isAdd: Boolean)

}