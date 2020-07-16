package com.chendroid.learning.api

import com.chendroid.learning.api.interceptor.AddCookieInterceptor
import com.chendroid.learning.api.interceptor.SaveCookieInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import loge
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @intro 创建 Retrofit 网络请求
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/1/18
 */
object ApiServiceHelper {

    private const val TAG = "RetrofitHelper"
    private const val CONTENT_PRE = "OkHttp: "
    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"
    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 10L

    val wanAndroidService: WanAndroidService = getService(Constant.REQUEST_BASE_URL, WanAndroidService::class.java)
    val newWanService = getService(Constant.REQUEST_BASE_URL, NewWanService::class.java)

    private fun create(url: String): Retrofit {
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {

            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            /**
             * 添加拦截器，获取 cookie
             */
            addInterceptor(SaveCookieInterceptor())

            /**
             * 添加 cookie
             */
            addInterceptor(AddCookieInterceptor())

            // add log print 调试时可用于查看网络请求的 message 信息
            if (Constant.INTERCEPTOR_ENABLE) {
                // loggingInterceptor
                addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    loge(TAG, CONTENT_PRE + it)
                }).apply {
                    // log level
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }

            // 添加网络拦截， 用于 chrome 的 inspect
            addNetworkInterceptor(StethoInterceptor())
        }

        return RetrofitBuilder(
                url = url,
                client = okHttpClientBuilder.build(),
                gsonFactory = GsonConverterFactory.create(),
                coroutineCallAdapterFactory = CoroutineCallAdapterFactory()
        ).retrofit
    }

    /**
     * get ServiceApi
     */
    private fun <T> getService(url: String, service: Class<T>): T = create(url).create(service)

}


/**
 *  创建 Retrofit.Builder
 */
class RetrofitBuilder(
        url: String, client: OkHttpClient,
        gsonFactory: GsonConverterFactory,
        coroutineCallAdapterFactory: CoroutineCallAdapterFactory) {

    val retrofit: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(gsonFactory)
        addCallAdapterFactory(coroutineCallAdapterFactory)
    }.build()

}