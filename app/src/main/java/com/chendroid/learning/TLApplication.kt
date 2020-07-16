package com.chendroid.learning

import android.app.Application
import android.content.Context
import android.util.Log
import com.chendroid.learning.base.Preference
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho

/**
 * @intro application 类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-15
 */
class TLApplication : Application() {


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.i("zc_test", "applicationContext is $applicationContext" + " _and context is " + baseContext)
    }

    override fun onCreate() {
        super.onCreate()

        Log.i("zc_test", "applicationContext is $applicationContext" + " _and context is " + baseContext)

        // 注册 Stetho inspector
        Stetho.initializeWithDefaults(this)
        Fresco.initialize(this)
        Preference.setContext(applicationContext)
    }

}