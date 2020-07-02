package com.chendroid.demo;

import android.util.Log;

/**
 * @author zhaochen@ZhiHu Inc.
 * @intro
 * @since 2020/6/22
 */
class TestInstance {
    private volatile static TestInstance instance;

    private TestInstance() {}

    public static TestInstance getInstance() {
        if (instance == null) {
            synchronized (TestInstance.class) {
                if (instance == null) {
                    instance = new TestInstance();
                }
            }
        }

        return instance;
    }
}
