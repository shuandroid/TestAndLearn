package com.chendroid.learning.ui.activity

import android.os.Bundle
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.chendroid.learning.R

/**
 * @intro 尝试所有的事情的界面
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-10-09
 */
class TryEveryThingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("zc_test", " TryEveryThingActivity onCreate()")

//        setContentView(R.layout.activity_try_everything)
//
//        Log.i("zc_test", " TryEveryThingActivity onCreate() layout 结束后")

        // 异步加载 xml
        AsyncLayoutInflater(this).inflate(R.layout.activity_try_everything, null, object : AsyncLayoutInflater.OnInflateFinishedListener {
            override fun onInflateFinished(view: View, p1: Int, p2: ViewGroup?) {
                setContentView(view)
                Log.i("zc_test", " TryEveryThingActivity onCreate() AsyncLayoutInflater 结束后")

            }
        })
    }


}