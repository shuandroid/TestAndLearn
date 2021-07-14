package com.chendroid.preinflater

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import java.lang.reflect.Field
import java.util.*

/**
 * @author      : zhaochen
 * @date        : 2021/7/9
 * @description : 异步初始化 layout
 */
class AsyncWrapperLayoutInflater(context: Context) {

    private val TAG = "AsyncWrapperLayoutInflater"


    private var asyncInflater: AsyncLayoutInflater = AsyncLayoutInflater(context)

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    private val viewPool = HashMap<Int, View>()

    private var isSetPrivateFactory = false

    companion object {
        @Volatile
        var instance: AsyncWrapperLayoutInflater? = null

        fun getInstance(context: Context): AsyncWrapperLayoutInflater {

            instance ?: synchronized(AsyncWrapperLayoutInflater::class.java) {
                instance ?: AsyncWrapperLayoutInflater(context).also { instance = it }
            }
//            if (instance == null) {
//                synchronized(AsyncWrapperLayoutInflater::class.java) {
//                    if (instance == null) {
//                        instance = AsyncWrapperLayoutInflater(context)
//                    }
//                }
//            }
            return instance!!
        }

    }

    fun preInflater(@LayoutRes layoutRes: Int) {
        asyncInflater.inflate(layoutRes, null) { view, layoutId, _ ->
            // 初始化好后，放入缓存池中
            viewPool[layoutId] = view
        }

        Log.d("zc_test", "Async preInflater() 预加载方方式")
    }

    /**
     * 获取 inflater 后的布局；
     * 1. 优先从 viewPool 中获取，
     * 2. 获取不到，则重新 inflater
     */
    fun inflater(@LayoutRes layoutRes: Int): View {
        viewPool[layoutRes]?.run {
            val targetView = viewPool.remove(layoutRes)
            Log.d("zc_test", "$TAG, inflater view  from viewPool")
            return targetView!!
        }

        Log.d("zc_test", "$TAG, inflater view  from inflater.inflate")
        return inflater.inflate(layoutRes, null, false)

    }

    fun wrapContext(newBase: Context): Context {
        return PreInflaterWrapper(newBase)
    }

    private inner class PreInflaterWrapper(base: Context) : ContextWrapper(base) {
        private var inflater: WrapperLayoutInflater? = null

        override fun getSystemService(name: String): Any {
            if (LAYOUT_INFLATER_SERVICE == name) {
                if (inflater == null) {
                    inflater = WrapperLayoutInflater(
                        super.getSystemService(name) as LayoutInflater,
                        this
                    )
                }

                return inflater!!
            }
            return super.getSystemService(name)
        }
    }


    private inner class WrapperLayoutInflater(
        val originalLayoutInflater: LayoutInflater,
        newContext: Context
    ) : LayoutInflater(originalLayoutInflater, newContext) {

        override fun cloneInContext(newContext: Context): LayoutInflater {

            return WrapperLayoutInflater(
                originalLayoutInflater.cloneInContext(newContext),
                newContext
            )
        }


        override fun setFactory(factory: Factory?) {
            super.setFactory(factory)
            originalLayoutInflater.factory = factory
        }

        override fun setFactory2(factory: Factory2?) {
            super.setFactory2(factory)
            originalLayoutInflater.factory2 = factory

            if (context is Factory2) {
                // 这里替换
                setPrivateFactoryInternal(originalLayoutInflater, context as Factory2)
            }

        }

        override fun inflate(resource: Int, root: ViewGroup?, attachToRoot: Boolean): View {
            val targetView = viewPool[resource]
            targetView?.run {
                if (attachToRoot && root != null) {
                    root.addView(this)
                }

                viewPool.remove(resource)
                return targetView
            }
            // 如果没有，则使用 inflater 去 inflate
            return inflater.inflate(resource, root, attachToRoot)
        }

    }


    /**
     * used to inflater fragment, so use activity getContext -> factory
     *
     * https://helw.net/2018/08/06/appcompat-view-inflation/
     *
     * part of createViewFromTag tries to get the view from the factory, and,
     * upon not finding it, falls back to mPrivateFactory, and finally falling
     * back to trying to create the class that the tag refers to. mPrivateFactory
     * is set by Activity in its constructor. (Interestingly enough, it is
     * this mPrivateFactory that is responsible for inflating fragments as seen here).
     *
     * @param inflater
     * @param factory
     */
    private fun setPrivateFactoryInternal(inflater: LayoutInflater, factory: Factory2) {
        if (isSetPrivateFactory) {
            return
        }
        var privateFactory: Field? = null
        try {
            privateFactory = LayoutInflater::class.java.getDeclaredField("mPrivateFactory")
            privateFactory.isAccessible = true
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        if (privateFactory != null) {
            try {
                val origin = privateFactory[inflater] as Factory2
                if (origin == null) {
                    privateFactory[inflater] = factory
                } else {
                    privateFactory[inflater] = PrivateWrapperFactory2(factory, origin)
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        isSetPrivateFactory = true
    }


    /**
     * 实现 Factory2 接口; 用来拦截 view 的初始化方式
     */
    private inner class PrivateWrapperFactory2(val factory2: Factory2, val origin: Factory2) :
        LayoutInflater.Factory2 {

        override fun onCreateView(
            parent: View?,
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? {

            val view = factory2.onCreateView(parent, name, context, attrs)
            Log.d(
                "zc_test",
                "Async PrivateWrapperFactory2 出 onCreateView() view is $view， and name is $name"
            )

            // 如果 view 为 null ,则使用 origin 重新创建 View

            return view ?: origin.onCreateView(parent, name, context, attrs)
        }

        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            val view = factory2.onCreateView(name, context, attrs)
            Log.d("zc_test", "Async PrivateWrapperFactory2 出 onCreateView() 第二个 view is $view")
            // 如果 view 为 null ,则使用 origin 重新创建 View
            return view ?: origin.onCreateView(name, context, attrs)
        }

    }


}