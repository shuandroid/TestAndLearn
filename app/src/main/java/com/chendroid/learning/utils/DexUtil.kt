package com.chendroid.learning.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.*
import java.lang.reflect.Array


object DexUtil {

    @SuppressLint("SdCardPath")
    fun test(context: Context) {

        val hostClassLoader = context.classLoader as PathClassLoader
        val hostPathList = ReflectorUtils.getPathList(hostClassLoader)
        // 得到 host  的 dexElements
        val hostDexElements = ReflectorUtils.getDexElements(hostPathList!!)
        Log.d("zc_test", "DexUtil hostDexElements is $hostDexElements")
//        context.assets.open()
        val realFile = File("/sdcard/Download/targetTest.dex")
        if (realFile.exists()) {
            Log.d("zc_test", " realFile 存在，路径为 is ${realFile.path}")
        } else {
            Log.d("zc_test", " realFile 不存在，不存在")
        }
        Log.d("zc_test", "context cache  file is ${context.cacheDir.path}")
        val targetFile = File(context.cacheDir.path + "targetTest.dex")
        if (targetFile.exists()) {
            Log.d("zc_test", "targetFile   file  存在， is ${targetFile.path}")
        } else {
            Log.d("zc_test", "targetFile   file  不不不不不存在")
        }
        copyFile(realFile, targetFile)

        if (targetFile.exists()) {
            Log.d("zc_test", "targetFile   file  存在， is ${targetFile.path}")
        } else {
            Log.d("zc_test", "targetFile   file  不不不不不存在")
        }

        val testClassLoader = createDexClassLoader(
            context,
            targetFile.path,
            context.cacheDir.path
        )
        Log.d("zc_test", "testClassLoader is $testClassLoader")

        val testPathList = ReflectorUtils.getPathList(testClassLoader)
        val testDexElements = ReflectorUtils.getDexElements(testPathList!!)
        Log.d("zc_test", "DexUtil testDexElements 里面的值～～～ is $testDexElements")

        //合并新的dexElement数组对象
        val newTempDexElements = combineArray(testDexElements, hostDexElements)

        ReflectorUtils.setField(hostPathList, hostPathList.javaClass, newTempDexElements)

        Log.d("zc_test", "setField 后")

        Log.d("zc_test", "在看看这时的 classLoader ${context.classLoader}")
    }

    fun copyFile(sourceFile: File, targetCacheFile: File) {
        // 新建文件输入流并对它进行缓冲
        val inputStream = FileInputStream(sourceFile)
        val inBuffer = BufferedInputStream(inputStream)

        // 新建文件输出流并对它进行缓冲
        val outputStream = FileOutputStream(targetCacheFile)
        val outBuffer = BufferedOutputStream(outputStream)

        // 缓冲数组
        val b = ByteArray(1024 * 5)
        var length = 0
        while (length != -1) {
            outBuffer.write(b, 0, length)
            length = inBuffer.read(b)
        }

        outBuffer.flush()
        inBuffer.close()
        outBuffer.close()
        outputStream.close()
        inputStream.close()
    }


    fun createDexClassLoader(
        context: Context,
        dexPath: String,
        optimizedPath: String
    ): DexClassLoader {

        val optimizedFile = File(optimizedPath)
        if (!optimizedFile.exists()) {
            optimizedFile.mkdirs()
        }
        // 创建出 classLoader
        return DexClassLoader(dexPath, optimizedPath, "", context.classLoader)
    }


    /**
     * 合并数组
     *
     * @param arrayLhs 前数组（插队数组）
     * @param arrayRhs 后数组（已有数组）
     * @return 处理后的新数组
     */
    fun combineArray(arrayLhs: Any?, arrayRhs: Any?): Any? {
        // 获得一个数组的Class对象，通过Array.newInstance()可以反射生成数组对象
        val localClass = arrayLhs!!.javaClass.componentType
        // 前数组长度
        val i: Int = Array.getLength(arrayLhs)
        // 新数组总长度 = 前数组长度 + 后数组长度
        val j: Int = i + Array.getLength(arrayRhs)
        // 生成数组对象
        val result: Any = Array.newInstance(localClass, j)
        for (k in 0 until j) {
            if (k < i) {
                // 从0开始遍历，如果前数组有值，添加到新数组的第一个位置
                val b: Any = Array.get(arrayLhs, k)
                Array.set(result, k, b)
                Log.d("zc_test", " 添加成功$b")
            } else {
                // 添加完前数组，再添加后数组，合并完成
                val b: Any = Array.get(arrayRhs, k - i)
                Array.set(result, k, b)
                Log.d("zc_test", b.toString())
            }
        }
        Log.d("zc_test", "合并完成， result size  is  $result")
        return result
    }


}