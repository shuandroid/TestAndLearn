package com.chendroid.learning.utils.cache

/**
 * @intro  使用双链表 + HashMap 实现LRUCache
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/15
 */
class LRUCacheHashMap {

    //定义双向链表节点
    private class ListNode(var key: Int, var value: Int) {
        var pre: ListNode? = null
        var next: ListNode? = null
    }

    private var capacity: Int = 10

    private val map = HashMap<Int, ListNode>()

    // 链表的头
    private val head = ListNode(-1, -1)

    // 链表的尾
    private val tail = ListNode(-1, -1)


    init {
        // 指向
        head.next = tail
    }

    fun get(key: Int): Int {

        if (!map.containsKey(key)) {
            return -1
        }

        val node = map[key]

        // 当前节点的前一个 next 指向 -> 当前节点的下一个 node
        node?.pre?.next = node?.next
        // 当前节点的下一个 pro 指向 -> 当前节点的上一个 node
        node?.next?.pre = node?.pre

        // 然后把该节点移动在链表的结尾
        moveToTail(node!!)

        return node.value

    }

    fun put(key: Int, value: Int) {

        // 直接调用 get(key) 方法；如果存在，则会被移动到 tail 节点， 再直接赋值即可
        if (get(key) != -1) {
            map[key]?.value = value

            return
        }

        // 不存在，则新建 ListNode
        val node = ListNode(key, value)
        map[key] = node
        moveToTail(node)
        // 上述，添加数据结束

        // 判断容量
        if (map.size > capacity) {
            // 移除第一个
            map.remove(head.next?.key)
            // 「head 的 next」 指向 ->  「head.next.next」节点
            head.next = head.next?.next
            // 「head.next.next」已经更新为 「head.next」 节点 的 pre 指向 -> 「head」 节点
            head.next?.pre = head
        }
    }

    /**
     * 把当前节点移动到结尾
     */
    private fun moveToTail(node: ListNode) {
        // 先把 「node 的 pre」 指向为 -> 「tail 尾点的上一个」
        node.pre = tail.pre
        // 在把 「tail 的 pre」 指向为 -> 「node」
        tail.pre = node
        // 把 「tail 尾点的上一个」 的 next -> 指向 「node」
        node.pre?.next = node
        // 把 「node.next」 指向为 -> 尾节点「tail」
        node.next = tail
    }

}