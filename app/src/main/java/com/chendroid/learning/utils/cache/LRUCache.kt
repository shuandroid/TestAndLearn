package com.chendroid.learning.utils.cache

import java.util.*
import kotlin.collections.HashMap

/**
 * @intro LRUCache 最近最少使用缓存 , 这里使用 HashMap + LinkedList 实现
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/9
 */
class LRUCache {

    // 缓存 map, 便于 put 时检索是否有相同数据
    /**
     * HashMap 里面数据是无序的，所以需要 LinkedList 来删除第一个元素； 也可使用 LinkedHashMap 实现 LRUCache
     */
    private lateinit var hashMap: HashMap<Int, Int>

    /**
     * 因为 hashMap 是无序的，所以这里需要使用 LinkedList 找到第一个被加入的数据，并移除；
     * LinkedList 便于插入和删除，
     * 但是查询较耗时「1. 不知道 index 的情况下，只能手动 forEach; 2. 知道 index , 使用 LinkedList.get(index)[也是遍历] 」
     */
    private lateinit var linkedList: LinkedList<Int>

    // 最大容量
    private var MAX_SIZE = 10

    fun LRUCache(capacity: Int) {
        MAX_SIZE = capacity
        hashMap = HashMap()
        linkedList = LinkedList()
    }

    /**
     * 因为 LinkedList get 查询操作耗时，这里使用 Map 去获取
     */
    fun get(key: Int): Int {

        if (!hashMap.containsKey(key)) {
            return -1
        }

        hashMap[key]?.run {
            val v = this
            // 利用 put 方法把该数据在 cache 列表中提前
            put(key, v)
            return v
        }

        return -1
    }

    /**
     * 在超过容量时，需要使用 linkedList.removeFirst, 移除第一个原素；因为 HashMap 无序
     *
     */
    private fun put(key: Int, value: Int) {

        // 如果已经有该数据
        if (hashMap.containsKey(key)) {
            hashMap[key]?.run {
                // 缓存队列中，删除该节点，并插入到尾部
                linkedList.remove(this)
                linkedList.addLast(key)
            }

            hashMap[key] = value
        } else {
            // 先判断是否达到 10 个最大容量判断
            if (MAX_SIZE == linkedList.size) {
                // 删除缓存列表中最后一个数据
                val last = linkedList.removeFirst()
                hashMap.remove(last)
            }

            // 新加的节点，直接添加到尾部
            linkedList.addLast(key)
            hashMap[key] = value
        }
    }


}