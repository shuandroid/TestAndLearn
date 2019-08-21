package com.chendroid.learning.vm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.chendroid.learning.bean.ArticleTagData
import com.chendroid.learning.repository.ArticleTagDataSource

/**
 * @intro MoreArticleTagFragment 类对应的 vm 类，处理数据和逻辑
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-21
 */
class MoreArticleTagViewModel(application: Application) : AndroidViewModel(application) {

    // 获取数据成功的 liveData
    val loadDataSuccess = MutableLiveData<List<ArticleTagData>>()

    // 获取数据失败的 liveData
    val loadDataFailed = MutableLiveData<String>()

    private val dataSource: ArticleTagDataSource by lazy {
        ArticleTagDataSource()
    }

    // 返回 LiveData 而不是 MutableLiveData 保证向外传输的数据只读
    fun onLoadDataSuccess(): LiveData<List<ArticleTagData>> = loadDataSuccess

    fun onLoadDataFailed(): LiveData<String> = loadDataFailed

    /**
     * 刷新数据
     */
    fun refreshData() {
        dataSource.getMoreArticleTreeList(object : ArticleTagDataSource.ArticleTagListCallback {

            override fun success(articleTagList: List<ArticleTagData>) {

                loadDataSuccess.postValue(articleTagList)
            }

            override fun error(errorMsg: String) {
                loadDataFailed.postValue(errorMsg)
            }
        })
    }
}