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
    private static void test(int [] targetNum, int n) {
//        int[] targetNum = new int[n];
        for(int i = 0; i < n; i++) {
            targetNum[i] = i;
        }
        //0 1 2 3 4...n-1
        for (int i = targetNum.length-1; i > 0; i--) {
            int r = new Random().nextInt(i+1);
            System.out.println(r);
            int tmp = targetNum[i];
            targetNum[i] = targetNum[r];
            targetNum[r] = tmp;
        }

    }

    public static void main(String[] args) {
//        Scanner cin = new Scanner(System.in);
//        int n = cin.nextInt();
        int[] arr = new int[10];
        test(arr, 10);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
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
            for (int i = 0;i < size;i++) {  //遍历上层节点（在此过程中，每个节点的值与最大值做比较，并将下层节点添加进来）
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



    private void test (int carNum, int[] a) {
        // 表明此时没有货物，不需要卡车
        if (a.length <= 0 ) {
            return;
        }
        // 此时货物的数量小于卡车的数量，题意不符合；
        if (a.length < carNum) {
            return;
        }

        // offset 表示每辆卡车必须装载一件货物后，剩余的货物数量
        int offset = a.length - carNum;
        // 把剩余的货物全放在一辆车上，该车会有 offset + 1 件货物；
        // 并且它的所有的货物的价值都是在该数组中 a[] 是价值大的货物

        // 对数组 a 排序, 价值大的放在前面；，只需要筛选出前 offset + 1 个货物

        // 晒选条件变成
        for (int i = 0; i < offset + 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                //
                if (a[i] < a[j]){
                    // 交换位置
                    // i[k] 和 i[k+1] 交换
                    int temp;
                    temp = a[j];
                    a[j] = a[i];
                    a[i] = temp;
                }
            }
        }

        // 此时数组 a 中，前 offset + 1 个 是价值大的货物；
        // count 表示前 offset + 1 的总和
        int count = 0;
        for (int i = 0; i < offset + 1; i++) {
            count = count + a[i];
        }

        //
        // max 用来存储最大的价值, 平方;
        int max = count * count;


    }






}
