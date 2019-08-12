package com.chendroid.learning.api

import android.support.annotation.VisibleForTesting
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.HashMap
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

/**
 * @intro 封装网络库的基本使用
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-12
 */
class Net {

    private val mServiceMap = ConcurrentHashMap<Class<*>, Any>()
    private val mWrapperScheduler = Schedulers.from(WrapperExecutor())


    private val sRetrofit by lazy {
        val builder = Retrofit.Builder()
        builder.client(OkHttpClient())
        builder.baseUrl("https://wanandroid.com/")
        builder.addConverterFactory(GsonConverterFactory.create())
        builder.addCallAdapterFactory(CoroutineCallAdapterFactory())
        builder.build()
    }

    /**
     * 创建 Retrofit service。
     */
    fun createService(service: Class<Any>): Any {
        var serviceImpl = mServiceMap[service]
        if (serviceImpl == null) {
            serviceImpl = createWrapperService(sRetrofit, service)
            mServiceMap[service] = serviceImpl
        }
        return serviceImpl
    }

    private fun createWrapperService(retrofit: Retrofit, service: Class<Any>): Any {

        return Proxy.newProxyInstance(service.classLoader, arrayOf<Class<*>>(service), object : InvocationHandler {
            override fun invoke(proxy: Any, method: Method, args: Array<out Any>): Any {

                if (method.returnType === Observable::class.java) {
                    return Observable.defer {
                        val service2 = getRetrofitService()
                        val ob = getRetrofitMethod(service2, method).invoke(service2, *args) as Observable<*>

                        ob.subscribeOn(Schedulers.io())
                    }.subscribeOn(mWrapperScheduler)
                }

                val service2 = getRetrofitService()
                return getRetrofitMethod(service2, method).invoke(service2, *args)
            }

            private var originToProxyMethod: Map<Method, Method> = HashMap(20)

            @Throws(NoSuchMethodException::class)
            private fun getRetrofitMethod(service: Any, origin: Method): Method {
                var proxy: Method? = originToProxyMethod[origin]
                if (proxy == null) {
                    proxy = service.javaClass.getMethod(
                            origin.name, *origin.parameterTypes)
                    putMethod(origin, proxy)
                }
                return proxy!!
            }

            /**
             * Copy-On-Write
             */
            @Synchronized
            private fun putMethod(origin: Method, proxy: Method) {
                val newMap = HashMap(originToProxyMethod)
                newMap[origin] = proxy
                originToProxyMethod = newMap
            }


            @Volatile
            private var retrofitService: Any? = null

            private fun getRetrofitService(): Any {
                retrofitService?.run {
                    return this
                }

                synchronized(this) {
                    retrofitService?.run {
                        return this
                    }
                    retrofitService = retrofit.create(service)
                }

                return retrofitService!!
            }

        })
    }


    /**
     * RetrofitWrapper 用的 Executor
     */
    @VisibleForTesting
    internal class WrapperExecutor : Executor {

        private val mExecutor: ThreadPoolExecutor

        init {
            mExecutor = ThreadPoolExecutor(
                    CORE_POOL_SIZE, CORE_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                    LinkedBlockingQueue(), THREAD_FACTORY)
            mExecutor.allowCoreThreadTimeOut(true)
        }

        override fun execute(runnable: Runnable) {
            mExecutor.execute(runnable)
        }

        companion object {
            private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
            private val CORE_POOL_SIZE = max(1, CPU_COUNT)
            private const val KEEP_ALIVE_SECONDS = 30L

            private val THREAD_FACTORY = object : ThreadFactory {
                private val mCount = AtomicInteger(1)

                override fun newThread(r: Runnable): Thread {
                    return Thread(r, "RetrofitWrapperThread #" + mCount.getAndIncrement())
                }
            }
        }
    }

}