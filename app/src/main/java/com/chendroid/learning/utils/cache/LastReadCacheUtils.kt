package com.chendroid.learning.utils.cache

import java.util.*
import kotlin.collections.ArrayList

/**
 * @intro 最近浏览的数据缓存工具类
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/5
 */
object LastReadCacheUtils {


    // todo 尝试实现一个 LRUCache 缓存队列

    // 回答默认最大的的缓存数量
    private const val LAST_READ_ARTICLE_MAX_DEFAULT = 10

    // 用来存储最近浏览回答的 id， 默认最多只有 10 个;
    private val lastArticleList: LinkedList<Long> by lazy {
        LinkedList<Long>()
    }

    /**
     *  添加正在看的文章 id
     */
    @JvmStatic
    fun addAiticleId(currentArticleId: Long) {

        // 要先判断是否有和 currentAnswerId 重复的值， 有的话，则去除掉
        for (id: Long in lastArticleList) {
            if (id == currentArticleId) {
                lastArticleList.remove(id)
                break
            }
        }

        // 比较是否大于默认值，如果大于，则 remove 最先加入的值
        if (lastArticleList.size > LAST_READ_ARTICLE_MAX_DEFAULT) {
            lastArticleList.removeFirst()
        }
        // 添加进去新的浏览值
        lastArticleList.offer(currentArticleId)
    }



    val list1 = ArrayList<Int>()
    val list2 = ArrayList<Int>(20)




}