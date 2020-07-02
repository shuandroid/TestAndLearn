package com.chendroid.demo;

/**
 * @author zhaochen@ZhiHu Inc.
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





}
