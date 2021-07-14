package com.chendroid.preinflater

import android.content.Context
import android.util.Log
import androidx.annotation.IdRes
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java8.util.stream.StreamSupport

/**
 * @author      : zhaochen
 * @date        : 2021/7/9
 * @description : 预加载处理类
 */
object PreInflaterManager {

    private const val IO_THREAD = "io"
    private const val MAIN_THREAD = "main"

    /**
     * io 的参数组
     */
    val ioParams = Pair<String, Scheduler>(IO_THREAD, Schedulers.io())

    /**
     * main 的参数组
     */
    val mainParams = Pair<String, Scheduler>(MAIN_THREAD, AndroidSchedulers.mainThread())

    // 需要在 IO 线程初始化的 layout 的 id 集合
    val preInflaterOnIoLayoutList = arrayListOf<Int>()

    // 需要在 MAIN 线程初始化的 layout 的 id 集合
    val preInflaterOnMainLayoutList = arrayListOf<Int>()

    @JvmStatic
    fun init(context: Context) {

        // 初始化
        collectPreInflater()
        // 开始真正初始化
        executePreInflater(context)
    }

    /**
     * 预留方法，在 ASM 中添加代码
     */
    @JvmStatic
    fun collectPreInflater() {

    }

    @JvmStatic
    fun executePreInflater(context: Context) {
        Log.d("zc_test", "PreManager executePreInflater 开始加载 pre")
        val inflater = AsyncWrapperLayoutInflater.getInstance(context)

        Completable.fromRunnable {
            StreamSupport.stream(preInflaterOnIoLayoutList).distinct()
                .forEach { res -> inflater.preInflater(res) }
        }.subscribeOn(ioParams.second).subscribe()

        Completable.fromRunnable {
            StreamSupport.stream(preInflaterOnMainLayoutList).distinct()
                .forEach { res -> inflater.preInflater(res) }
        }.subscribeOn(mainParams.second).subscribe()

    }

    @JvmStatic
    fun addPreInflateInfo(info: PreInflateInfo) {
        Log.d("zc_test", "添加 pre layout 布局, info $info")
        if (ioParams.first == info.scheduler) {
            preInflaterOnIoLayoutList.add(info.layout)
        }

        if (mainParams.first == info.scheduler) {
            preInflaterOnMainLayoutList.add(info.layout)
        }
    }


    class PreInflateInfo(@param:IdRes var layout: Int, var scheduler: String)

}