package com.chendroid.learning.vm

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chendroid.care.data.Result
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.data.repo.CollectArticleRepo
import com.chendroid.learning.data.source.CollectArticleDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @intro 有关收藏文章的 viewModel
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/4/15
 */
class CollectArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val collectArticleRepo =
        CollectArticleRepo(CollectArticleDataSource(ApiServiceHelper.newWanService))

    /**
     * 收藏文章
     */
    fun collectArticle(articleId: Int, handleResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = collectArticleRepo.collectArticle(articleId)
            if (result is Result.Success) {
                withContext(Dispatchers.Main) {
                    handleResult(true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    handleResult(false)
                    Toast.makeText(getApplication(), "失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 取消收藏文章
     */
    fun unCollectArticle(articleId: Int, originId: Int, handleResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = collectArticleRepo.unCollectArticle(articleId, originId)

            if (result is Result.Success) {
                withContext(Dispatchers.Main) {
                    handleResult(true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    handleResult(false)
                    Toast.makeText(getApplication(), "失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}