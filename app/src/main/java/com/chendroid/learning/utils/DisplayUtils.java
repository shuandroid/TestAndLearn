package com.chendroid.learning.utils;

import android.content.Context;

/**
 * @author zhaochen @ Zhihu Inc.
 * @intro
 * @since 2019/1/8
 */
public class DisplayUtils {


    public static int dpToPixel(final Context pContext, final float pDp) {
        if (pContext == null) {
            return 0;
        }

        final float density = pContext.getResources().getDisplayMetrics().density;

        return (int) ((pDp * density) + 0.5f);
    }

    private class ListNode {
        int key;
        int val;
        ListNode pre;
        ListNode next;

        public ListNode(int key, int val) {
            this.key = key;
            this.val = val;
            pre = null;
            next = null;
        }
    }

}
