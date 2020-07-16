package com.chendroid.learning.utils.cache

/**
 * @intro 使用 LinkedHashMap 实现 LRI
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/15
 */
class LRUCacheLinkedHashMap {

    // 容量大小，默认为 10
    var capacity = 10

    private var map: LinkedHashMap<Int, Int> = LinkedHashMap()


    fun get(key: Int): Int {
        if (!map.containsKey(key)) {
            return -1
        }

        // 先移除
        val value = map.remove(key) as Int
        // 再重新加入，每次访问该数据，都需要更新该数据的位置为最新
        map.put(key, value)

        return value
    }

    fun put(key: Int, value: Int) {

        if (map.containsKey(key)) {
            map.remove(key)
            map.put(key, value)
            return
        }

        map.put(key, value)

        // 检测是否超出容量： capacity
        if (map.size > capacity) {
            // 利用迭代器得到第一个的 key「因为 LinkedHashMap 是有序的」， 然后移除
            map.remove(map.entries.iterator().next().key)
        }

    }


}