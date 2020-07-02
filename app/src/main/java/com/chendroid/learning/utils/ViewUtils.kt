package com.chendroid.learning.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.view.PixelCopy
import android.view.View
import androidx.annotation.RequiresApi
import java.lang.Exception

/**
 * @intro 有关 View 的一些工具方法
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/6/30
 */
object ViewUtils {

    /**
     * getDrawingCache 被抛弃，使用 PixelCopy
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchBitmapFromView(view: View, activity: Activity, callback: (Bitmap) -> Unit) {

        if (activity.window == null) {
            return
        }

        if (view.width <= 0 || view.height <= 0) {
            throw Exception("view 的宽和高，不能小于 0")
        }

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        val locationOfViewInWindow = IntArray(2)
        view.getLocationInWindow(locationOfViewInWindow)

        try {
            PixelCopy
                    .request(activity.window!!, Rect(locationOfViewInWindow[0],
                            locationOfViewInWindow[1],
                            locationOfViewInWindow[0] + view.width,
                            locationOfViewInWindow[1] + view.height),
                            bitmap, { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            callback(bitmap)
                        }
                    }, Handler())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}