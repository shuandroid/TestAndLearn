package com.chendroid.learning

import android.app.Application
import com.chendroid.learning.base.Preference
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho

/**
 * @intro application 类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-15
 */
class TLApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 注册 Stetho inspector
        Stetho.initializeWithDefaults(this)
        Fresco.initialize(this)

        Preference.setContext(applicationContext)
    }

}