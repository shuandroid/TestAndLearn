package com.chendroid.learning.vm

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chendroid.care.data.Result
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.bean.BaseDatas
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.data.repo.FirstHomeWanRepo
import com.chendroid.learning.data.source.FirstHomeWanDataSource
import com.chendroid.learning.data.usecase.GetBannerUseCase
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @intro 首页的 ViewModel 处理数据类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-11-29
 */
class FirstHomeViewModel : ViewModel() {

    private val _bannerUILD = MutableLiveData<List<HomeBanner.BannerItemData>>()

    // 对外暴露的 LD
    val bannerUILD: LiveData<List<HomeBanner.BannerItemData>>
        get() = _bannerUILD

    private val vmArticleLD = MutableLiveData<List<BaseDatas>>()

    // 文章的 live data
    val articleLD: LiveData<List<BaseDatas>>
        get() = vmArticleLD

    // 是否在拉去数据
    var isLoadingArticle = false

    private val getBannerUseCase by lazy {
        GetBannerUseCase()
    }

    private val firstHomeWanRepo by lazy {
        FirstHomeWanRepo(FirstHomeWanDataSource(ApiServiceHelper.newWanService))
    }


    /**
     * 获取首页 banner 信息
     */
    fun getBannerData() {
        viewModelScope.launch {
            val result = getBannerUseCase.getWanAndroidBanner()

            if (result is Result.Success) {
                withContext(Main) {
                    emitUIBanner(result.data)
                }
            }
        }

    }

    /**
     *  在 ui  现场中调用，刷新 banner
     */
    @UiThread
    private fun emitUIBanner(banners: List<HomeBanner.BannerItemData>) {
        _bannerUILD.value = banners
    }

    /**
     * 获取文章列表
     */
    fun getArticleList(page: Int = 0) {

        isLoadingArticle = true

        viewModelScope.launch {
            val result = firstHomeWanRepo.getArticle(page)

            if (result is Result.Success) {
                result.data.datas?.run {
                    withContext(Main) {
                        emitUIArticleList(this@run)
                    }
                }

            }
        }
    }

    /**
     * 刷新文章列表
     */
    @UiThread
    private fun emitUIArticleList(articleList: List<BaseDatas>) {
        isLoadingArticle = false
        vmArticleLD.value = articleList
    }


}