package com.chendroid.learning.demo

import android.util.Log
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * @intro 测试 suspend 关键字和 coroutines
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-10
 */
object TestSuspend {
//
//    suspend fun mainTest() {
//        println("mainTest() start start start " + Thread.currentThread())
//        a()
//        b()
//        c()
//        println("mainTest() end end end" + Thread.currentThread())
//    }
//
//    fun test2() {
//        println("test2() doing doing doing " + Thread.currentThread())
//    }
//
//    fun a() {
//        println("a() doing doing doing " + Thread.currentThread())
//    }
//
//    fun c() {
//        println("c() doing doing doing " + Thread.currentThread())
//    }
//
//    suspend fun b() {
//        val test = "hahaa"
//        println("b() start start start" + Thread.currentThread())
//        coroutineScope {
//            println("11111 线程 是" + Thread.currentThread())
//            launch(Dispatchers.IO) {
//                println("22222 线程 是" + Thread.currentThread())
//                delay(1000)
//                println("22222 线程结束" + Thread.currentThread())
//            }
//            println("33333 线程 是" + Thread.currentThread())
//        }
//
//        println("b() end end end" + Thread.currentThread())
//    }
//
//    // 测试 async 协程的并发运行
//    suspend fun testAsync() {
//        coroutineScope {
//            val time = measureTimeMillis {
//                val one = async { doSomethingsOne() }
//                val two = async { doSomethingsTwo() }
//                println("the result is ${one.await() + two.await()}")
//            }
//
//            println("完成时间为 time is $time ms")
//        }
//    }
//
//    private suspend fun doSomethingsOne(): Int {
//        // 假设做了些事情，耗时
//        delay(1000L)
//        return 17
//    }
//
//    private suspend fun doSomethingsTwo(): Int {
//        // 假设做了些事情，耗时
//        delay(1000L)
//        return 30
//    }

    private fun testString(str: String, m: Int, testData: TestData) {

        str.let {
            Log.i("zc_test", "str let")
        }

        str.run {
            Log.i("zc_test", "str run")
        }

        testData.run {
            Log.i("zc_test", "hahhahaha  run")
        }

    }

    private fun testString2(str: String, m: Int, testData: TestData) {
        str.run {
            Log.i("zc_test", "str run")
        }

        testData.run {
            Log.i("zc_test", "hahhahaha  run")
        }
    }

    private fun testString3(str: String, m: Int, testData: TestData) {

        str.let {
            Log.i("zc_test", "str let")
        }
    }

    private fun testString4(str: String, m: Int, testData: TestData) {
        str.run {
            Log.i("zc_test", "str let")
        }
    }

    class TestData(id: String) {
    }


}