package com.chendroid.learning.vm

import android.app.Application
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.UiThread
import androidx.lifecycle.*
import com.chendroid.care.data.Result
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.bean.BaseDatas
import com.chendroid.learning.bean.HomeBanner
import com.chendroid.learning.data.repo.FirstHomeWanRepo
import com.chendroid.learning.data.source.FirstHomeWanDataSource
import com.chendroid.learning.data.usecase.GetBannerUseCase
import com.chendroid.learning.ui.holder.EmptyBannerData
import com.chendroid.learning.ui.holder.data.EmptyData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

/**
 * @intro 首页的 ViewModel 处理数据类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-11-29
 */
class FirstHomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "FirstHomeViewModel"
    }

    private val _bannerUILD = MutableLiveData<List<HomeBanner.BannerItemData>>()

    // 对外暴露的 LD
    val bannerUILD: LiveData<List<HomeBanner.BannerItemData>>
        get() = _bannerUILD

    private val vmArticleLD = MutableLiveData<List<BaseDatas>>()

    // 文章的 live data
    val articleLD: LiveData<List<BaseDatas>>
        get() = vmArticleLD

    val articleEmptyLD by lazy {
        MutableLiveData<EmptyData>()
    }
    val bannerEmptyLD by lazy {

        MutableLiveData<EmptyBannerData>()
    }

    // 是否在拉去数据
    var isLoadingArticle = false

    private val getBannerUseCase by lazy {
        GetBannerUseCase()
    }

    private val firstHomeWanRepo by lazy {
        FirstHomeWanRepo(FirstHomeWanDataSource(ApiServiceHelper.newWanService))
    }


    val testData: LiveData<List<BaseDatas>> = liveData {

        val result = firstHomeWanRepo.getArticle()
        if (result is Result.Success) {
//            emitSource()
            emit(result.data.datas!!)
        }
    }

    val bannerTest: LiveData<List<HomeBanner.BannerItemData>> = liveData(IO) {
        val result = getBannerUseCase.getWanAndroidBanner()
        if (result is Result.Success) {
            emit(result.data)
        }
    }


    /**
     * 获取首页 banner 信息
     */
    fun getBannerData() {

        viewModelScope.launch(IO) {
            val result = getBannerUseCase.getWanAndroidBanner()
            if (result is Result.Success) {
                withContext(Main) {
                    emitUIBanner(result.data)
                }
            } else if (result is Result.Error) {
                withContext(Main) {
                    emitUIEmptyBanner()
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

    private fun emitUIEmptyBanner() {
        bannerEmptyLD.value = EmptyBannerData("头图 banner")
    }

    /**
     * 获取文章列表
     */
    fun getArticleList(page: Int = 0) {

        isLoadingArticle = true
        viewModelScope.launch(IO) {
            val result = firstHomeWanRepo.getArticle(page)
            Log.i("zc_test", "11111 current thread is ${Thread.currentThread()}")
            if (result is Result.Success) {
                result.data.datas?.run {
                    withContext(Main) {
                        Log.i("zc_test", "22222 current thread is ${Thread.currentThread()}")
                        emitUIArticleList(this@run)
                    }
                    Log.i("zc_test", "33333 current thread is ${Thread.currentThread()}")
                }
            } else if (result is Result.Error) {
                withContext(Main) {
                    Log.i("zc_test", "$TAG get artile error , mes is ${result.toString()}")
                    emitUIArticleEmpty()
                }
            }
        }
    }

//    val testStateFlow: StateFlow<List<BaseDatas>> =  flow {
//
//        val result = firstHomeWanRepo.getArticle(0)
//        if (result is Result.Success) {
//            emit(result.data.datas)
//        } else if (result is Result.Error) {
//            emit(null)
//        }
//    }.stateIn(viewModelScope)

    /**
     * 刷新文章列表
     */
    @UiThread
    private fun emitUIArticleList(articleList: List<BaseDatas>) {
        isLoadingArticle = false
        vmArticleLD.value = articleList
    }

    /**
     * 文章为空的界面
     */
    @UiThread
    private fun emitUIArticleEmpty() {
        isLoadingArticle = false
        articleEmptyLD.value = EmptyData()
    }

}