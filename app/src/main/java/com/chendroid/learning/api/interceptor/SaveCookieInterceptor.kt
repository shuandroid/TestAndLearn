package com.chendroid.learning.api.interceptor

import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.base.Preference
import encodeCookie
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @intro cookie 拦截器
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/4/16
 */
class SaveCookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url().toString()
        val domain = request.url().host()

        // set-cookie maybe has multi, login to save cookie, 如果请求中包含用户登陆信息，则缓存用户的 cookie 信息
        if ((requestUrl.contains(ApiServiceHelper.SAVE_USER_LOGIN_KEY) || requestUrl.contains(
                        ApiServiceHelper.SAVE_USER_REGISTER_KEY
                ))
                && !response.headers(ApiServiceHelper.SET_COOKIE_KEY).isEmpty()) {
            val cookies = response.headers(ApiServiceHelper.SET_COOKIE_KEY)
            val cookie = encodeCookie(cookies)
            saveCookie(requestUrl, domain, cookie)
        }

        return response
    }

    /**
     * save cookie to SharePreferences
     */
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    private fun saveCookie(url: String?, domain: String?, cookies: String) {
        url ?: return
        var spUrl: String by Preference(url, cookies)
        @Suppress("UNUSED_VALUE")
        spUrl = cookies
        domain ?: return
        var spDomain: String by Preference(domain, cookies)
        @Suppress("UNUSED_VALUE")
        spDomain = cookies
    }
}