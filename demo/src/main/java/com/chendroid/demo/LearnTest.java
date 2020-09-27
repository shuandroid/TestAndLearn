package com.chendroid.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
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

    // 生成长度为n的int型随机数组，数组元素范围为0~n-1，每个元素都是唯一的。只使用基本数据类型。
    private static void test(int[] targetNum, int n) {
//        int[] targetNum = new int[n];
        for (int i = 0; i < n; i++) {
            targetNum[i] = i;
        }
        //0 1 2 3 4...n-1
        for (int i = targetNum.length - 1; i > 0; i--) {
            int r = new Random().nextInt(i + 1);
            System.out.println(r);
            int tmp = targetNum[i];
            targetNum[i] = targetNum[r];
            targetNum[r] = tmp;
        }

    }

    // 层序遍历二叉树，遍历的过程中保存最大值
    public List<Integer> largestValues(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        LinkedList<TreeNode> linkedList = new LinkedList<>();   //保存每层的节点
        linkedList.addFirst(root);
        while (!linkedList.isEmpty()) {
            int max = Integer.MIN_VALUE;    //保存最大值
            int size = linkedList.size();   //size表示上一层节点的数目
            for (int i = 0; i < size; i++) {  //遍历上层节点（在此过程中，每个节点的值与最大值做比较，并将下层节点添加进来）
                TreeNode node = linkedList.pollLast();
                if (node.value > max) {   //如果节点的值大于max，更新max
                    max = node.value;
                }
                if (node.leftNode != null) {    //将左节点添加到linkedList中
                    linkedList.addFirst(node.leftNode);
                }
                if (node.rightNode != null) {   //将右节点添加到linkedList中
                    linkedList.addFirst(node.rightNode);
                }
            }
            result.add(max);    //将该层的最大值保存到结果中
        }

        ArrayList<Integer> test = new ArrayList<>(10);
        test.set(1, test.get(2));
        test.size();
        return result;
    }

    private static void testListToTwo(ListNode node) {

        if (node == null) {
            return;
        }
        // 头
        ListNode head = node.nextNode;
        // 奇数
        ListNode temp1 = node;
        ListNode first = temp1;
        // 偶数
        ListNode temp2 = node.nextNode;
        ListNode first2 = temp2;
        while (temp1 != null && temp2 != null && temp1.nextNode != null && temp2.nextNode != null) {
            temp1.nextNode = temp1.nextNode.nextNode;
            temp2.nextNode = temp2.nextNode.nextNode;
            temp1 = temp1.nextNode;
            temp2 = temp2.nextNode;
        }
        // 前面为奇数，后面为偶数
//        temp1.nextNode = head;

        // return node 为完整的一条链表；

//        while (node != null) {
////            System.out.println(node.value);
////            node = node.nextNode;
////        }
        // 需要设置 temp1.nextNode = null;
        while (first != null) {
            System.out.println("enenen 第一个");
            System.out.println(first.value);
            first = first.nextNode;
        }
        while (first2 != null) {
            System.out.println("enenen 第二个");
            System.out.println(first2.value);
            first2 = first2.nextNode;
        }

    }

    public static void main(String[] args) {

//        ListNode node1 = new ListNode();
//        node1.value = 1;
//        ListNode node2 = new ListNode();
//        node2.value = 2;
//        ListNode node3 = new ListNode();
//        node3.value = 3;
//        ListNode node4 = new ListNode();
//        node4.value = 4;
//
//        ListNode node5 = new ListNode();
//        node5.value = 5;
//
//        ListNode node6 = new ListNode();
//        node6.value = 6;
//
//        node1.nextNode = node2;
//        node2.nextNode = node3;
//        node3.nextNode = node4;
//        node4.nextNode = node5;
//        node5.nextNode = node6;
//
//        testListToTwo(node1);


        TestString testString = new TestString();
        testString.setTest(null);

    }


    private void intTest() {
        int test = Integer.parseInt("34");
        List<Integer> intLoist = new ArrayList<>();

    }




}
