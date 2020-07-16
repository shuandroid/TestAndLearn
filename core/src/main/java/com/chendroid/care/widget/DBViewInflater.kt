package com.chendroid.care.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat

/**
 * @intro 替换 View
 * @author zhaochen@ZhiHu Inc.
 * @since 2020-02-12
 */
class DBViewInflater {


    inner class LayoutInflaterFactoryWrapper : LayoutInflater.Factory2 {


        override fun onCreateView(p0: View?, p1: String, p2: Context, p3: AttributeSet): View? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCreateView(p0: String, p1: Context, p2: AttributeSet): View? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


    /**
     * 通过抢先设置Factory的方式，要在super.onCreate(savedInstanceState)之前调用，应该用这个更安全
     */
    fun installViewFactory(activity: AppCompatActivity) {
        val delegate = activity.delegate
//        if (delegate is LayoutInflater.Factory2) {
//            val factory = delegate as LayoutInflater.Factory2
//            val wrapper = LayoutInflaterFactoryWrapper(factory, activity.window)
//            val layoutInflater = LayoutInflater.from(activity)
//            if (layoutInflater.factory == null) {
//                LayoutInflaterCompat.setFactory2(layoutInflater, wrapper)
//            }
//        }
    }


    /**
     *  仿照 AppCompatViewInflater 的 createView()
     */
    fun createView(parent: View, name: String, @NonNull context: Context,
                   @NonNull attrs: AttributeSet, inheritContext: Boolean,
                   readAndroidTheme: Boolean, readAppTheme: Boolean, wrapContext: Boolean) {

    }


}