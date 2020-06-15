package com.chendroid.learning.utils.cache

/**
 * @intro 使用单链表 + HashMap 实现 LRUCache
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/15
 */
class LRUCacheList {

    //定义单链表节点
    private class ListNode(var key: Int, var value: Int) {
        var next: ListNode? = null
    }

    private var capacity: Int = 10

    private val map = HashMap<Int, ListNode>()

    // 单链表的头
    private val head = ListNode(-1, -1)

    // 单链表的尾
    private var tail = head


    fun get(key: Int): Int {

        if (!map.containsKey(key)) {
            return -1
        }

        // 当前
        val targetPre = map[key]

        return -1
    }


    /**
     * 移动到尾部
     */
    private fun moveToTail(node: ListNode) {
        // 先把 node.next 置为 null
        node.next = null
        // tail.next 指向 -> node 节点
        tail.next = node
        // 在把 tail 节点后
        tail = tail.next!!
    }

}