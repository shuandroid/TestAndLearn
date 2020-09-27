package com.chendroid.demo;

import java.util.Arrays;

/**
 * @author zhaochen@ZhiHu Inc.
 * @intro
 * @since 2020/9/8
 */
class TestString {

    public void setTest(Object object) {
        System.out.println("setTest(Object object) 这里");
    }

    public void setTest(String string) {
        System.out.println("setTest(String string) 这里");
    }

//    public void setTest(Integer integer) {
//        System.out.println("setTest(Integer integer) 这里");
//
//    }

    private void test2() {
        String test = "anbcencven";
        int one = test.charAt(0) - 47;
    }

    // numbers 5 个
    public boolean isContinuous(int [] numbers) {
        //
        if(numbers.length != 5) {
            return false;
        }

        int specialCount = 0;
        int normalCount = 0;
        int first = numbers[0];

        for (int i = 0; i < 5; i++) {
            // 大小王
            if(numbers[i] == 0) {
                specialCount++;
            } else {
                //
                normalCount++;


            }

        }

        int[] tempArray = new int[normalCount];
        int j = 0;

        for(int i = 0; i < 5; i++) {
            if(numbers[i] != 0) {
                tempArray[j] = numbers[i];
                j++;
            }
        }

        //
        //
        Arrays.sort(tempArray);

        if(specialCount == 4) {
            return true;
        }
        //
        int testCount = specialCount;
        //
        for(int i = 0 ; i< normalCount - 1; i++) {
            if(testCount < 0) {
                return false;
            }

            if(tempArray[i + 1] - tempArray[i] == 0) {
                return false;
            }

            if(tempArray[i + 1] - tempArray[i] - 1 > testCount) {
                return false;
            } else {
                testCount =  testCount - (tempArray[i + 1] - tempArray[i] - 1);
            }

        }

        if(testCount < 0) {
            return false;
        }

        return true;
    }

    private void test2(int[] nums) {

    }

}
