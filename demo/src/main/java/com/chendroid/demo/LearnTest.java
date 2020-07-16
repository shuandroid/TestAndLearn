package com.chendroid.demo;

import java.util.Stack;

/**
 * @author zhaochen@ZhiHu Inc. 算法二三
 * @intro test
 * @since 2020/6/19
 */
class LearnTest {


    /**
     * 移除有序数组中的重复元素，并且返回新数组的长度
     * 切记不可改变在中间插入数组元素;
     * [0, 1, 1, 2, 2, 3, 3 ] -> [0, 1, 2, 3] 返回 4
     */
    private int reSortNumArray(int[] nums) {

        if (nums.length == 0) {
            return 0;
        }

        int n = nums.length;

        // 快慢指针
        int slow = 0;
        int fast = 1;

        while (fast < n) {

            if (nums[slow] != nums[fast]) {
                slow++;
                nums[slow] = nums[fast];
            }

            fast++;
        }

        return slow + 1;
    }


    // 反转二叉树, 递归
    private TreeNode reverseTree(TreeNode root) {

        if (root == null) {
            return root;
        }

        TreeNode temp = root.leftNode;

        root.leftNode = reverseTree(root.rightNode);
        root.rightNode = reverseTree(temp);
        return root;
    }

    // 反转二叉树，
    private TreeNode reverseTree2(TreeNode root) {

        if (root == null) {
            return root;
        }

        Stack<TreeNode> stack = new Stack<>();

        stack.push(root);

        while (!stack.isEmpty()) {

            TreeNode node = stack.peek();
            stack.pop();

            if (node.leftNode != null) {
                stack.push(node.leftNode);
            }

            if (node.rightNode != null) {
                stack.push(node.rightNode);
            }

            TreeNode temp = node.leftNode;
            node.leftNode = node.rightNode;

            node.rightNode = temp;

        }

        return root;
    }

    static class TreeNode {
        int value;
        TreeNode leftNode;
        TreeNode rightNode;
    }

    // 单
    static class ListNode {
        int value;
        ListNode nextNode;
    }

    // 反转单链表
    private ListNode reverseNode(ListNode node) {
        ListNode pre, cur;
        pre = null;
        cur = node;

        // pre 是存放的反转后的链表的头
        while (cur != null) {
            ListNode next = cur.nextNode;
            cur.nextNode = pre;

            pre = cur;
            cur = next;
        }

        return pre;

    }


}
