package com.chendroid.care.net

import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap


/**
 * @intro 网络相关，代理
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/4/9
 */
object Net {

    // 存储 service 的 Map
    private val SERVICE_MAP: Map<Class<*>, Any> = ConcurrentHashMap()

    private val retrofit by lazy {

    }

    init {

    }

//    @Suppress("UNCHECKED_CAST")
//    fun <T> createService(service: Class<T>): T {
//
//        var serviceImpl = SERVICE_MAP[service] as T
//        serviceImpl?.run {
//            return serviceImpl
//        }
//
//        serviceImpl = createWrapperService()
//
//        return createWrapperService(service)
//    }
//
//    fun <T> createWrapperService(retrofit: Retrofit, service: Class<T>): T {
//
//
//    }

}