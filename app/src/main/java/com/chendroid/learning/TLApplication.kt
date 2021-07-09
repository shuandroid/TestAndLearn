package com.chendroid.learning

import android.app.Application
import android.content.Context
import android.util.Log
import com.chendroid.learning.base.Preference
import com.chendroid.learning.utils.DexUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import leakcanary.LeakCanary


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
        val applicationClassLoader  =  TLApplication::class.java.classLoader
        Log.i("zc_test", " TLApplication classLoader is ${applicationClassLoader.toString()}")
//        classLoader
        packageName
        Log.i("zc_test", " TLApplication getPackageName is ${packageName}")

        Log.i("zc_test", " TLApplication classLoader  parent is ${classLoader.parent}")

//        val testContext = applicationContext.createPackageContext("com.p1.mobile.putong", CONTEXT_INCLUDE_CODE or CONTEXT_IGNORE_SECURITY)

//        Log.i("zc_test", " TLApplication testContext classLoader   is ${testContext.classLoader}, and hash code id is ${testContext.classLoader.hashCode()} ")

        // 注册 Stetho inspector
        Stetho.initializeWithDefaults(this)
        Fresco.initialize(this)
        Preference.setContext(applicationContext)
        // TODO: 2021/7/9 暂时注释调，这里不使用他
//        DexUtil.test(this)
    }


}